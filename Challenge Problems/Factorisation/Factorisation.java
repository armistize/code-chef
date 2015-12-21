import java.io.*;
import java.math.BigInteger;
import java.util.*;

/**
 * @author tawit_k
 *
 */
public class Factorisation {
	public static void main(String[] s) {
		try {
			InputStream inputStream = System.in;
			InputReader in = new InputReader(inputStream);
			PrintWriter writer = new PrintWriter(System.out);

			TaskFactorisation solution = new TaskFactorisation();
			solution.solve(in, writer);

			writer.close();
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class InputReader {
	public BufferedReader reader;
	public StringTokenizer tokenizer;

	public InputReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
		tokenizer = null;
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}

	public double nextDouble() {
		return Double.parseDouble(next());
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}

	public long nextLong() {
		return Long.parseLong(next());
	}

}

class TaskFactorisation {
	private List<BigInteger> bigIntPrimes;
	private List<Integer> intPrimes;
	private int limit = 1000000;

	private List<String> getFactors(String number) {
		if (number.length() < 18) {
			long num = Long.parseLong(number);
			return getFactorsForLongNos(num);
		}

		if (number.length() < 3000) {
			return getFactorsForBigIntegers(number);
		}

		return Arrays.asList(new String[] { number });
	}

	private <K, V extends Integer> List<String> getFactorsAsString(
			Map<K, V> factorsCount) {
		List<String> factors = new ArrayList<String>();

		for (Map.Entry<K, V> entry : factorsCount.entrySet()) {
			K factor = entry.getKey();
			V count = entry.getValue();

			for (int i = 0; i < count.intValue(); i++) {
				factors.add(factor + "");
			}
		}

		return factors;
	}

	private List<String> getFactorsForBigIntegers(String num) {
		BigInteger number = new BigInteger(num);
		int totalfactorsTested = 2000;
		Map<String, Integer> factorsCount = new HashMap<String, Integer>();

		for (int i = 0; i < totalfactorsTested; i++) {
			BigInteger prime = bigIntPrimes.get(i);
			int k = 0;
			while (number.mod(prime).compareTo(BigInteger.ZERO) == 0) {
				number = number.divide(prime);
				k++;
			}

			if (k > 0) {
				factorsCount.put(prime.toString(), k);
			}
		}

		if (number.compareTo(BigInteger.ONE) > 0) {
			factorsCount.put(number.toString(), 1);
		}

		return getFactorsAsString(factorsCount);

	}

	private List<String> getFactorsForLongNos(long num) {
		int factorsLimit = 3000;
		Map<Long, Integer> factorsCount = new HashMap<Long, Integer>();

		for (int prime : intPrimes) {

			if (prime > num / prime)
				break;

			int k = 0;
			while (num % prime == 0) {
				num /= prime;
				k++;
			}

			if (k > 0) {
				factorsCount.put((long) prime, k);
				factorsLimit--;
				if (factorsLimit == 0)
					break;
			}
		}

		if (num > 1)
			factorsCount.put(num, 1);

		return getFactorsAsString(factorsCount);
	}

	private void initBigIntPrimes() {
		bigIntPrimes = new ArrayList<BigInteger>();
		for (int i = 0; i < intPrimes.size(); i++) {
			bigIntPrimes.add(new BigInteger(String.valueOf(intPrimes.get(i))));
		}
	}

	private void initPrimes() {
		int root = (int) Math.sqrt(limit) + 1;

		boolean[] primes = new boolean[limit];
		Arrays.fill(primes, true);

		for (int i = 2; i < root; i++) {
			for (int j = i * i; j < limit; j += i) {
				primes[j] = false;
			}
		}

		intPrimes = new ArrayList<Integer>();
		intPrimes.add(2);
		for (int i = 3; i < primes.length; i += 2) {
			if (primes[i])
				intPrimes.add(i);
		}

	}

	public void solve(InputReader in, PrintWriter out) throws IOException {
		initPrimes();
		initBigIntPrimes();

		int tests = in.nextInt();

		for (int i = 0; i < tests; i++) {
			String number = in.next();

			List<String> factors = getFactors(number);
			out.println(factors.size());

			for (String factor : factors) {
				if (factor.equals("1")) {
					throw new RuntimeException("Incorrect");
				}
				out.println(factor);
			}
		}

	}

}

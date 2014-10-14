import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Main{
    /*
    0F9184626272C6A3
    0323A0F04F9C2409
    0E70D68977C49E51
    04F3C9FCADCB63ED
    0A80341B13BDFEE5
    0B293A5FB2DE4D6B
    091D5B5A275D9AA2
    02C004BAAF622421
    0997F64331F439F9
    0CACB15656F6F836
    064BBDA99B7D5E9E
    0AA00A1977CB9F29
    0F0231A69441B70A
    0851B6608461ACE4
    0580BFE550CD366B
    06A4197E1AA5FE5C
    0725439733DCA8AD
    038FBE8C4BB0B921
    08B8B575C94847FB
    0478DFB56BC69800
    01BBC39686E97054
    03A89842DBC06968
    */

    private static final int INPUT_LENGTH = 4194304;
    private static int currentProgress = 0;

    public static void main(String[] args) throws Exception {
        long[] h = getH();
        Hashtable<Integer, ArrayList<Long>> hashTable = new Hashtable<Integer, ArrayList<Long>>();


        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input.txt"))));
        for (int i = 0; i < INPUT_LENGTH; i++) {
            long input = Long.parseLong(bf.readLine(), 16);
            int hash = hash(h, input);
            if (hashTable.get(hash) == null) {
                hashTable.put(hash, new ArrayList<Long>());
            }
            hashTable.get(hash).add(input);
        }
        bf.close();

        // Generate new hashFunctions
        Hashtable<Integer, long[]> hashFunctions = new Hashtable<Integer, long[]>();
        int counter = 0;
        int keySetSize = hashTable.keySet().size();
        for (int hash : hashTable.keySet()) {
            counter++;
            printProgress(counter, keySetSize);

            ArrayList<Long> collisions = hashTable.get(hash);
            if (collisions.size() > 2) {
                double log2 = Math.log(collisions.size())/Math.log(2);
                int numRows = (int) Math.ceil(log2);
                long[] hashFunction;
                int[] tempTable;

                do {
                    hashFunction = getRandomHashFunction(numRows);
                    tempTable = new int[(int) Math.pow(2, numRows)];
                    for (Long value : collisions) {
                        int newHash = hash(hashFunction, value);
                        tempTable[newHash]++;
                    }
                } while (!isPerfectHash(tempTable));

                hashFunctions.put(hash, hashFunction);
            }
        }
        printToFile(hashFunctions, hashTable);
    }

    private static boolean isPerfectHash(int[] tempTable) {
        for (int i = 0; i < tempTable.length; i++) {
            if (tempTable[i] > 1) {
                return false;
            }
        }
        return true;
    }

    private static long[] getRandomHashFunction(int numRows) {
        Random rand = new Random();
        long[] hashFunction = new long[numRows];
        for (int i = 0; i < hashFunction.length; i++) {
            hashFunction[i] = rand.nextLong();
        }
        return hashFunction;
    }

    private static int hash(long[] h, long input) {
        int hash = 0;
        for (int i = 0; i < h.length; i++) {
            long longHash = input & h[i];
            hash = hash | Long.bitCount(longHash) % 2;
            hash = hash << 1;
        }
        hash = hash >> 1;
        return hash;
    }

    public static long[] getH() throws Exception {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("h.txt"))));
        long[] h = new long[22];

        for (int i = 0; i < 22; i++) {
            h[i] = Long.parseLong(bf.readLine(), 16);
        }

        bf.close();
        return h;
    }

    private static void printProgress(int counter, int keySetSize) {
        double percent = (double) counter/keySetSize * 100;
        int progress = (int) percent;
        if (progress - 9 > currentProgress) {
            System.out.println((int) percent + "% done...");
            currentProgress = progress;
        } else if (counter == 1) {
            System.out.println("Started generating hash functions");
        }
    }

    private static void printToFile(Hashtable<Integer, long[]> hashFunctions, Hashtable<Integer, ArrayList<Long>> hashTable) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("output.txt"))));
        for (int hash : hashFunctions.keySet()) {
            long[] hashFunction = hashFunctions.get(hash);
            bw.write(hash + " " + hashFunction.length);
            bw.newLine();

            for (long l : hashFunction) {
                bw.write(Long.toHexString(l).toUpperCase());
                bw.newLine();
            }
        }
        bw.close();
    }
}
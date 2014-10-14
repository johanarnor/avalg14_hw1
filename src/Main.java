import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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

    private static int INPUT_LENGTH = 4194304;

    public static void main(String[] args) throws Exception {
        long[] h = getH();
        long[] hashTable = new long[INPUT_LENGTH];

        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("input.txt"))));
        for (int i = 0; i < INPUT_LENGTH; i++) {
            long input = Long.parseLong(bf.readLine(), 16);
            int hash = hash(h, input);
            hashTable[hash]++;
        }

        print(hashTable);
    }

    private static void print(long[] hashTable) {
        int sum = 0;
        int prints = 0;
        int noZeros = 0;
        int noOnes = 0;
        for (int i = 0; i < hashTable.length; i++) {
            if (hashTable[i] > 8) {
//                System.out.println(i + "|" + hashTable[i]);
                prints++;
            }
            if (hashTable[i] == 0) {
                noZeros++;
            }
            if (hashTable[i] == 1) {
                noOnes++;
            }

            sum += hashTable[i];
        }

        System.out.println("NO ZEROS = " + noZeros);
        System.out.println("NO ONES = " + noOnes);
        System.out.println("NO PRINTS = " + prints);
        System.out.println("SUM = " + sum);
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

        return h;
    }
}
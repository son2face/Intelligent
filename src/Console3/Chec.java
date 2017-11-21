package Console3;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Chec {
    static Cloner cloner = new Cloner();

    /* Returns true if the there is a subarray of arr[] with sum equal to
       'sum' otherwise returns false.  Also, prints the result */
    void quicksortWeight(List<Integer> pairShapes, int low, int high) {
        int i = low, j = high;
        int pivot = pairShapes.get(low + (high - low) / 2);
        while (i <= j) {
            while (pairShapes.get(i) > pivot) {
                i++;
            }
            while (pairShapes.get(j) < pivot) {
                j--;
            }
            if (i <= j) {
                int temp = pairShapes.get(i);
                pairShapes.set(i, pairShapes.get(j));
                pairShapes.set(j, temp);
                i++;
                j--;
            }
        }
        if (low < j)
            quicksortWeight(pairShapes, low, j);
        if (i < high)
            quicksortWeight(pairShapes, i, high);
    }

    static List<List<Integer>> findAllArr(List<Integer> list,int startIndex, int currentSum, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = startIndex; i < list.size(); i++) {
            if (list.get(i) + currentSum > sum) break;
            List<Integer> a = new ArrayList<>();
            if (list.get(i) + currentSum == sum) {
                a.add(list.get(i));
                result.add(a);
                for (int j = i + 1; j < list.size(); j++) {
                    if (Objects.equals(list.get(j), list.get(i))) {
                        List<Integer> t = new ArrayList<>();
                        t.add(list.get(j));
                        result.add(t);
                    } else {
                        break;
                    }
                }
                break;
            } else {
                int newSum = currentSum + list.get(i);
                Integer num = cloner.deepClone(list.get(i));
                List<List<Integer>> lists = findAllArr(list,i+1, newSum, sum);
                lists.parallelStream().forEach(list1 -> {
                    list1.add(num);
                });
                result.addAll(lists);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Chec arraysum = new Chec();
        Integer arr[] = {15, 2, 1, 4, 8, 4, 4, 16, 4, 9, 10, 24};
        int n = arr.length;
        int sum = 16;
        List<Integer> s = Arrays.asList(arr);
//        quicksortWeight(groupPair, 0, groupPair.size() - 1);
//        groupPair.forEach(x -> {
//            System.out.println(x);
//        });
        List<Integer> a = new ArrayList<>();
        for(int i = s.size()-1; i>=0; i --){
            a.add(s.get(i));
        }
        List<List<Integer>> list = findAllArr(a,0,0,sum);
        list.forEach(list1 -> {
            list1.forEach(i->{
                System.out.println(i);
            });
            System.out.println();
            System.out.println();
        });
//        arraysum.subArraySum(arr, n, sum);

    }
}


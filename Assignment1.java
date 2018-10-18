import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class Assignment1 {
	
public static void main(String[] args) {
		
		int [] runsizes = new int[] {5, 10, 15, 20, 30, 50, 100};
		MergeSort merge = new MergeSort();
		ConvolutedSort conv = new ConvolutedSort();
		
		
		// test sorting methods on randomArray, compare for each run_size
		for(int i=0; i<runsizes.length; i++) {
			int [] randomArray = IntStream.generate(() -> new Random().nextInt(100)).limit(500000).toArray();
			System.out.println("\nrun_size = " + runsizes[i]);
			
			// time & record ConvolutedSort
			long convoluted_start = System.nanoTime();
			conv.sort(randomArray, runsizes[i]);
			long convoluted_end = System.nanoTime();
			long conv_dur = (convoluted_end - convoluted_start)/1000000;
			System.out.println("convoluted sort time: " + conv_dur + "ms");
			
			// time & record ConvolutedSort
			long merge_start = System.nanoTime();
			merge.sort(randomArray);
			long merge_end = System.nanoTime();
			long merge_dur = (merge_end - merge_start)/1000000;
			System.out.println("merge sort time: " + merge_dur + "ms");
		}
	}
}
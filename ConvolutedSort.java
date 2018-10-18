import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class ConvolutedSort {
	
	
	public void sortFrom(int [] arr, int start, int last) {
		// performs insertion sort on segment of array, starting and ending at specified indices (inclusive)
		
		int [] temp = new int [last-start+1]; // copy segment into temp array
		for(int i=start; i<=last; i++) {
			temp[i-start] = arr[i];
		}
		
		for(int i=1; i<temp.length; i++) { // insertion sort temp array
			int check = temp[i];
			int j = i-1;
			while((j>=0) && (temp[j]>check)) {
				temp[j+1] = temp[j];
				j = j-1;
			}
			temp[j+1] = check;
		}
		
		for(int i=start; i<=last; i++) { // copy sorted segment back into original array
			arr[i] = temp[i-start];
		}
	}
	
	
	public int[] getRuns(int [] arr, int run_size) {
		// finds runs (>= run_size) in array, sorts unsorted segments into runs (with sortFrom()), returns array of run indices
		
		int [] indices = new int [arr.length]; // initializes array to store the start index of every segment
		int num_runs = 0;
		int start = 0;
		int last_stop = 0;
		int i = 0;
	
		while(i < arr.length) {
	
			start = i; // sets the start of the next possible run
	
			// unsorted length of run_size means we should sort it and keep going.
			if(start-last_stop > run_size) {	
				// IN-PLACE SORT FROM INDICES: (last_stop --> last_stop + run_size - 1).
				// this sorts segment of size run_size. if leftover values, just continue
				sortFrom(arr, last_stop, last_stop+run_size-1);
				last_stop = last_stop+run_size-1;
				num_runs++; // adding NEWLY SORTED run
				indices[num_runs] = last_stop; // marks the starting index of the next segment we get
			}
			
			// keeps updating i while discovering a run
			while(i+1 < arr.length) {
				if(arr[i+1] >= arr[i]) {
					i++;
				}
				else {
					break;
				}
			}
			
			// true if a run is found above and is long enough
			if(i-start >= run_size) {				
				// true if there is a previous, unsorted section - it will be <= run_size
				if(start != last_stop) {
					// IN-PLACE SORT FROM (last_stop --> start-1). this sorts the section just before the run
					sortFrom(arr, last_stop, start-1);
					num_runs++; // adding NEWLY SORTED run
					last_stop = start; // this last_stop will be overwritten by the end of the run found UNLESS the run that was found finished the array. then this is the last starting index
					indices[num_runs] = last_stop;
				}
				// at this point everything up to the run (start --> i) we found has been sorted. document the run found, increment and move on
				if(i<arr.length-1) { // if false, this run was the last segment and we are finished
					num_runs++; // adding NATURAL run
					last_stop = i+1; // (i+1) will also be the next start value
					indices[num_runs] = last_stop;
				}
			}
			i++;
		}
		if(last_stop < start) { // true if there is a segment at the end which needs to be sorted
			sortFrom(arr, last_stop, arr.length-1); // sort to the end of the array. this last_stop has already been put into "indices" array
			// num_runs++;
			// indices[num_runs] = arr.length;
		}
		return indices;
		// NOW OUR ARRAY IS COMPOSED OF RUNS. USE RUNLIST TO MERGE SORT OUR ARRAY
	}

	public void merge(int [] arr, int left_idx, int left_end, int right_idx, int right_end) {
		// merge two specified segments (indices inclusive) into temp array
		int [] temp = new int[left_end-left_idx+right_end-right_idx+2];
		int start = left_idx;
		int temp_idx = 0;
		while((left_idx<=left_end) && (right_idx<=right_end)) {
			if(arr[left_idx] <= arr[right_idx]) {
				temp[temp_idx++] = arr[left_idx++];
			}
			else {
				temp[temp_idx++] = arr[right_idx++];
			}
		}
		// once on exhausts, add the rest of the other
		while(left_idx<=left_end) {
			temp[temp_idx++] = arr[left_idx++];
		}
		while(right_idx<=right_end) {
			temp[temp_idx++] = arr[right_idx++];
		}
		// copy back from temp into original arr
		for(int i=0; i<temp.length; i++) {
			arr[start+i] = temp[i];
		}
	}
	
	public void mergeRuns(int [] arr, int [] indices) {
		// takes in array and run indices, performs merge sort on runs to complete sorting

		int n=0; // this will be the number of segments
		boolean more = true;
		while(more && n<indices.length) {
			n++;
			more = false;
			if(indices[n]!=0) {
				more = true;
			}
		}
		indices[n]=arr.length;

		// if n is odd, merge the last two segments so that merge sort can be done on an even set
		if(n%2==1) {
			merge(arr, indices[n-2], indices[n-1]-1, indices[n-1], indices[n]-1); // NOTE: arr[n] = 0, arr[n-1] = last segment start, arr[n-2] = second to last segment start
			indices[n-1] = arr.length;
			n--; // this is still the number of segments
		}

		// now we have an even amount of runs to perform merge sort on
		while(n>1) {
			for(int k=0; k<n; k+=2) { // merge adjacent segments
				merge(arr, indices[k], indices[k+1]-1, indices[k+1], indices[k+2]-1);
			}
			for(int k=0; k<=n/2; k++) { // fix indices array to contain new relevant indices
				indices[k] = indices[k*2];
			}
			n/=2;
			// catch case of n becoming odd, in which case we need to merge two segments again. this either happens once or not at all
			if(n%2==1 && n>1) { // same process as before
				merge(arr, indices[n-2], indices[n-1]-1, indices[n-1], indices[n]-1);
				indices[n-1] = arr.length;
				n--;
			}
		}
	}
	
	public void sort(int [] arr) {
		// default run_size to 16 if not specified
		sort(arr, 16);
	}
	
	public void sort(int [] arr, int run_size) {
		// System.out.println("unsorted array: "+Arrays.toString(arr));
		int[] indices = getRuns(arr, run_size);
		mergeRuns(arr, indices);
		// System.out.println("sorted array: "+Arrays.toString(arr));
	}
}
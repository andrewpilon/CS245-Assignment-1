// Andrew Pilon

public class MergeSort {
	
	public void sort(int [] arr) {
		int [] temp = new int[arr.length]; // this is so that we don't have to keep making new arrays
		mergesort(arr, temp, 0, arr.length-1);
	}
	
	public void mergesort(int [] arr, int [] temp, int lower, int upper) {
		if(lower>=upper) { // check if we should be finished with sorting
			return;
		}
		int mid = (lower+upper)/2;
		mergesort(arr, temp, lower, mid);
		mergesort(arr, temp, mid+1, upper);
		merge(arr, temp, lower, upper);
	}
	
	public void merge(int [] arr, int [] temp, int lower, int upper) {
		// LEFT ARRAY: lower -> lower_end
		// RIGHT ARRAY: upper_start -> upper
		int lower_end = (lower+upper)/2;
		int idx = lower;// initiate indices at start of: main array...
		int lower_idx = lower; // ...left merge array
		int upper_idx = lower_end + 1; // ...right merge array
		
		while(lower_idx<=lower_end && upper_idx<=upper) {
			if(arr[lower_idx]<=arr[upper_idx]) { // add lower arr value and increment lower_idx
				temp[idx] = arr[lower_idx];
				lower_idx++;
			}
			else { // implies arr[lower_idx]>arr[upper_idx], add upper arr value and increment upper_idx
				temp[idx] = arr[upper_idx];
				upper_idx++;
			}
			idx++; //increment temp counter after every addition
		}
		// now copy left and right arrays into temp
		System.arraycopy(arr, lower_idx, temp, idx, lower_end-lower_idx+1);
		System.arraycopy(arr, upper_idx, temp, idx, upper-upper_idx+1);
		// then temp into original array
		System.arraycopy(temp, lower, arr, lower, upper - lower + 1);
		
	}
}
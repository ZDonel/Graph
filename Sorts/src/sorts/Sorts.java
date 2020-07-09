package sorts;

public class Sorts {
	
	public static void bubSort(int[] arr) {//switch consecutive elements in arr if they are out of order
		int t;
		for(int i = arr.length-1; i > 0; i--){
			for (int j = 0; j < i; j++) {
				if(arr[j] > arr[j+1]){
					t = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = t;
				}	
			}
		}
	}
	
	public static void insSort(int[] arr) {//sort from beginning of array
		int t;
		for(int i = 1; i < arr.length; i++){
			for (int j = i; j > 0; j--) {
				if(arr[j] < arr[j-1]){
					t = arr[j];
					arr[j] = arr[j-1];
					arr[j-1] = t;
				}	
			}
		}
	}
	
	// public static int[] quickSort(int[] arr) {//choose pivot and sort around
	// 	if(arr.length == 1){
	// 		return arr;
	// 	} else if(arr.length == 2){
	// 		if(arr[0] > arr[1]){
	// 			int t = arr[0];
	// 			arr[0] = arr[1];
	// 			arr[1] = t;
	// 		}
	// 		return arr;
	// 	} else {
	// 		// int[] a = new int[arr.length/2];
	// 		// int[] b = new int[arr.length - (arr.length/2)];
	// 		// for (int i = 0; i < arr.length; i++) {
	// 		// 	if(i < arr.length/2){
	// 		// 		a[i]=arr[i];
	// 		// 	}else {
	// 		// 		b[i-arr.length/2] = arr[i];
	// 		// 	}
	// 		// }
	// 		// a = quickSort(a);
	// 		// b = quickSort(b);
	// 		// int x = 0,y = 0;
	// 		// while(x+y < arr.length){
	// 		// 	if(x < a.length && y < b.length){
	// 		// 		if(a[x] <= b[y]){
	// 		// 			arr[x+y] = a[x++];
	// 		// 		}else {
	// 		// 			arr[x+y] = b[y++];
	// 		// 		}
	// 		// 	} else if(x == a.length){
	// 		// 		arr[x+y] = b[y++];
	// 		// 	} else if(y == b.length){
	// 		// 		arr[x+y] = a[x++];
	// 		// 	}
	// 		// }
	// 		return arr;
	// 	}
	// }
	
	public static int[] mergeSort(int[] arr) {//break arr in half and mergeSort each half
		if(arr.length == 1){
			return arr;
		} else if(arr.length == 2){
			if(arr[0] > arr[1]){
				int t = arr[0];
				arr[0] = arr[1];
				arr[1] = t;
			}
			return arr;
		} else {
			int[] a = new int[arr.length/2];
			int[] b = new int[arr.length - (arr.length/2)];
			for (int i = 0; i < arr.length; i++) {
				if(i < arr.length/2){
					a[i]=arr[i];
				}else {
					b[i-arr.length/2] = arr[i];
				}
			}
			a = mergeSort(a);
			b = mergeSort(b);
			int x = 0,y = 0;
			while(x+y < arr.length){
				if(x < a.length && y < b.length){
					if(a[x] <= b[y]){
						arr[x+y] = a[x++];
					}else {
						arr[x+y] = b[y++];
					}
				} else if(x == a.length){
					arr[x+y] = b[y++];
				} else if(y == b.length){
					arr[x+y] = a[x++];
				}
			}
			return arr;
		}
	}
	
	public static int[] heapSort(int[] arr){
		MaxHeap heap = new MaxHeap(arr);
		heap.heapSort();
		return heap.heap;
	}

}

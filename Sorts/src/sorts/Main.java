package sorts;

public class Main {

	public static void main(String[] args) {
		int[] arr = {3, 5, 1, 35, 2, 6, 60, 20, 103, 6, 98, 28, 2, 55, 21, 0, 39, 73};
		arr = Sorts.heapSort(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}		
	}
	
}

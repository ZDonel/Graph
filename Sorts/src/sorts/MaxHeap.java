package sorts;


//maxheap class for heap sort
public class MaxHeap {
    int[] heap;
    int size;

    //returns empty maxheap of size n
    public MaxHeap(int n){
        heap = new int[n];
        size = 0;
    }

    //creates heap from given array
    public MaxHeap(int[] arr){
        heap = new int[arr.length];
        size = 0;
        for (int i : arr) {
            push(i);
        }
    }

    //finds left child index
    public int leftChild(int i){
        return i*2+1;
    }

    //finds right child index
    public int rightChild(int i) {
        return i*2+2;
    }

    //finds parent index
    public int parent(int i) {
        return (i-1)/2;
    }

    //simple swap function
    public void swap(int x, int y){
        int t = heap[x];
        heap[x] = heap[y];
        heap[y] = t;
    }

    //enter elements into heap if there is space
    public void push(int i) {
        if(heap.length == size) {
            System.out.println("no space in heap");
            return;
        }
        heap[size] = i;
        int c = size++;
        while(c > 0 && i > heap[parent(c)]){
            swap(c, parent(c));//swap with parent
            c = parent(c);
        }
    }

    //remove element from heap
    public int pop(){
        int head = heap[0];
        heap[0] = heap[--size];
        heap[size] = head;
        heapify(0);
        return head;
    }

    //old function
    // public void maxHeapify(){
    //     if (size < 2) {
    //         return;
    //     } else if(size == 2){
    //         if(heap[0] < heap[1])
    //             swap(0, 1);
    //         return;
    //     }
    //     for (int i = (this.size/2) -1; i >= 0; i--) {
    //         heapify(i);   
    //     }
    // }

    //moves elements into correct heap orientation
    private void heapify(int i) {
        int root = i;
        int l = leftChild(i);
        int r = rightChild(i);
        if(l < size && heap[l] > heap[root]){
            root = l;
        } 
        if(r < size && heap[r] > heap[root]){
            root = r;
        }
        if(i != root){
            swap(i, root);
            heapify(root);
        }
    }

    //sort array by popping elements to the end of the array until empty
    public void heapSort(){
		while(size > 0){
            pop();
        }
    }
    
    public String toString(){
        String heapString = "[ ";
        for (int i = 0; i < heap.length - 1; i++) {
            heapString += heap[i] + ", ";
        }
        heapString += heap[heap.length-1] + " ]";
        return heapString;
    }

}
package sorts;

public class MaxHeap {
    int[] heap;
    int size;

    public MaxHeap(int n){//returns empty maxheap of size n
        heap = new int[n];
        size = 0;
    }

    public MaxHeap(int[] arr){
        heap = new int[arr.length];
        size = 0;
        for (int i : arr) {
            push(i);
        }
    }

    public int leftChild(int i){
        return i*2+1;
    }

    public int rightChild(int i) {
        return i*2+2;
    }

    public int parent(int i) {
        return (i-1)/2;
    }

    public void swap(int x, int y){
        int t = heap[x];
        heap[x] = heap[y];
        heap[y] = t;
    }

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

    public int pop(){
        int head = heap[0];
        heap[0] = heap[--size];
        heap[size] = head;
        heapify(0);
        return head;
    }

    public void maxHeapify(){
        if (size < 2) {
            return;
        } else if(size == 2){
            if(heap[0] < heap[1])
                swap(0, 1);
            return;
        }
        for (int i = (this.size/2) -1; i >= 0; i--) {
            heapify(i);   
        }
    }

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

    public void heapSort(){//heapify int max heap and delete elements to end of arr to sort
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
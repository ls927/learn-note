package datastructure;

/**
 * 手动实现红黑树
 */
public class RBTree<K extends Comparable<K>,V> {

    private int size;
    private Node root;

    public RBTree(){
        this.size = 0;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getSize() {
        return size;
    }


    /**
     * 节点
     */
    class Node{

        public static final boolean BLACK = true;
        public static final boolean RED = false;

        private K key;
        private V value;

        private boolean color;
        private int N;
        private Node left;
        private Node right;
        private Node parent;

        public Node(){}

        public Node(boolean color){
            this.color = color;
        }

        public Node(K key,V value,boolean color){
            this.key = key;
            this.value = value;
            this.color = color;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public boolean isBlack() {
            return color;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }
    }

    // 左旋
    public void leftRotate(Node node){

        if(node == null || node.right == null){
            return;
        }

        // 注意，旋转操作时，指针变化是必然是双向的，因为一个节点同时拥有 父节点指针 和 子节点指针
        // 所以以下操作都是 成对 的

        Node right = node.right;

        // 指认 父节点
        // 注意当 node 为根节点的情况
        if(node == root){

            right.parent = null;
            root = right;

        }else{

            Node parent = node.parent;
            right.parent = parent;
            if(parent.left == node){
                parent.left = right;
            }else {
                parent.right = right;
            }

        }


        node.right = right.left;
        // 注意右节点的左子节点为空的情况
        if(right.left != null){
            right.left.parent = node;
        }

        right.left = node;
        node.parent = right;

    }

    // 右旋
    public void rightRotate(Node node){

        if(node == null || node.left == null){
            return;
        }

        // 注意，旋转操作时，指针变化是必然是双向的，因为一个节点同时拥有 父节点指针 和 子节点指针
        // 所以以下操作都是 成对 的

        Node left = node.left;


        // 注意当 node 为根节点的情况
        if(node == root){

            left.parent = null;
            root = left;

        }else{

            Node parent = node.parent;
            left.parent = parent;

            if (parent.left == node){
                parent.left = left;
            }else {
                parent.right = left;
            }

        }

        node.left = left.right;
        // 注意左节点的右子节点为空的情况
        if(left.right != null){
            left.right.parent = node;
        }

        left.right = node;
        node.parent = left;


    }

    // 后续节点
    public Node successorOf(Node node){

        if(node == null || node.right == null) return null;

        Node right = node.right;
        Node successor = right;
        while (successor.left != null){
            successor = successor.left;
        }

        return successor;

    }

    // 加入元素
    // 先加入，后调整
    public void put(K k,V v){

        if(root == null){
            root = new Node(k,v,Node.BLACK);
            this.size ++;
            return;
        }

        Node cur = root;
        Node parent = null;
        while (cur != null){
            if(k.compareTo(cur.key) > 0){
                parent = cur;
                cur = cur.right;
            }else if(k.compareTo(cur.key) < 0){
                parent = cur;
                cur = cur.left;
            }else{
                cur.value = v;
                return;
            }
        }

        Node node = new Node(k, v, Node.RED);
        node.parent = parent;
        if(k.compareTo(parent.key) > 0){
            parent.right = node;
        }else {
            parent.left = node;
        }

        this.size ++;

        //调整
        fixAfterPut(node);

    }


    // 加入节点后调整
    private void fixAfterPut(Node node) {

        // 第一种情况（2-Node）：即若父节点为黑，不需任何调整
        if(node.parent.color == Node.BLACK) return;

        // 第二和第三种情况
        while (node != root && node.parent != null && node.parent.color != Node.BLACK){

            Node parent = node.parent;
            // 左
            if(parent.parent.left == parent){
                // 第二种情况（3-Node）：
                if(parent.parent.right == null || parent.parent.right.color == Node.BLACK){
                    /* 1.
                     *     pp              pp              n(black)
                     *    /               /                 /  \
                     *   p(red)   -->    n(red)   -->  p(red)  pp(red)
                     *    \             /
                     *     n(red)      p(red)
                     */
                    if(parent.right == node){
                        // 涉及到 多个 旋转操作时，先声明一个副本引用指向要操作的对象，以免旋转造成影响
                        Node pp = parent.parent;

                        node.color = Node.BLACK;
                        pp.color = Node.RED;

                        leftRotate(parent);
                        rightRotate(pp);
                    }
                    /*
                     * 2.
                     *      pp               p(black)
                     *     /                / \
                     *    p(red)  -->  n(red)  pp(red)
                     *   /
                     *  n(red)
                     */
                    else {

                        Node pp = parent.parent;

                        parent.color = Node.BLACK;
                        pp.color = Node.RED;

                        rightRotate(pp);
                    }

                    node = root; //只是为了跳出循环
                }
                // 第三种情况：
                else{
                    /*
                     *        pp(black)                 pp(red)
                     *       /      \                   /    \
                     *     lp(red)  rp(red)  -->  lp(black)   rp(black)
                     *     /                         /
                     *   node(red)              node(red)
                     *
                     */
                    parent.color = Node.BLACK;
                    parent.parent.right.color = Node.BLACK;
                    parent.parent.color = Node.RED;

                    node = parent.parent; //向上回溯！

                }
            }
            // 右
            else{
                // 第二种情况（3-Node）：
                if(parent.parent.left == null || parent.parent.left.color == Node.BLACK){
                    /* 1.
                     *     pp              pp                n(black)
                     *       \               \                 /    \
                     *       p(red)   -->    n(red)   -->  pp(red)  p(red)
                     *      /                  \
                     *     n(red)              p(red)
                     */
                    if(parent.left == node){

                        Node pp = parent.parent;

                        node.color = Node.BLACK;
                        pp.color = Node.RED;

                        rightRotate(parent);
                        leftRotate(pp);
                    }
                    /*
                     * 2.
                     *      pp               p(black)
                     *        \                /    \
                     *        p(red)  --> pp(red)  n(red)
                     *          \
                     *         n(red)
                     */
                    else {

                        Node pp = parent.parent;

                        parent.color = Node.BLACK;
                        pp.color = Node.RED;

                        leftRotate(pp);

                    }

                    node = root; //只是为了跳出循环
                }
                // 第三种情况：
                else{
                    /*
                     *        pp(black)                 pp(red)
                     *       /      \                   /    \
                     *     lp(red)  rp(red)  -->  lp(black)   rp(black)
                     *                \                         \
                     *               node(red)                node(red)
                     *
                     */
                    parent.color = Node.BLACK;
                    parent.parent.left.color = Node.BLACK;
                    parent.parent.color = Node.RED;

                    node = parent.parent; //向上回溯！

                }

            }

        }

        root.color = Node.BLACK;


    }


    // 删除元素
    // 先删除，后调整
    public V remove(K key){

        if(key == null) return null;

        Node cur = root;
        while (cur != null){
            if(key.compareTo(cur.key) < 0){
                cur = cur.left;
            }else if(key.compareTo(cur.key) > 0){
                cur = cur.right;
            }else {
                break;
            }
        }

        if(cur == null) return null;
        V oldValue = cur.getValue();

        // 1.指定删除的节点带有 一个后继节点，寻找其后继节点
        if(cur.right != null){

            // 后续节点值覆盖
            Node replacement = successorOf(cur);
            cur.key = replacement.key;
            cur.value = replacement.value;

            // 若后继节点为红色，则其必为 = 叶子节点 =,即 情况一(3-Node'1) 和 情况二(4-Node)：
            /*
             *      |
             *     p(B)         root(B)
             *    /       or      \
             *  rep(R)           rep(R)
             *
             *      |
             *     p(B)            root(B)
             *    /   \     or     /   \
             * rep(R) right     left   rep(R)
             *
             * 直接删除即可
             */
            if(replacement.color == Node.RED){

                boolean isLeft = replacement == replacement.parent.left;
                if(isLeft) replacement.parent.left = null;
                else replacement.parent.right = null;

                replacement = null;

            }
            // 如果后继节点为黑色
            else{

                // 如果后继节点不为叶子节点，则其必有且只有一个右子节点,且该节点必为红色（由黑高推出），即情况二(3-Node'2)
                /*
                 *     p
                 *    /
                 *   rep(B)
                 *    \
                 *    right(R)
                 *
                 * 右子节点进行覆盖后并直接删除该节点
                 */
                if(replacement.right != null){
                    Node right = replacement.right;
                    replacement.key = right.key;
                    replacement.value = right.value;

                    replacement.right = null;
                    right = null;
                }
                // 如果后继节点为叶子节点，即情况四(2-Node)
                /*
                 *    |
                 *    p               root
                 *   /        or      /  \
                 *  rep(B)         left  rep(B)
                 *
                 * 分为 左子节点 和 右子节点 来分别处理（fixAfterRemove）
                 */
                else{
                    // 先调整，后删除
                    fixAfterRemove(replacement);

                    Node parent = replacement.parent;
                    if(parent.left == replacement) parent.left = null;
                    else parent.right = null;

                    replacement = null;
                }

            }
        }
        // 2. 指定删除节点没有后继节点
        else{

            // 如果为红色，其必为叶子节点
            if(cur.color == Node.RED){

                Node parent = cur.parent;
                if(parent.left == cur) parent.left = null;
                else parent.right = null;

                cur = null;

            }
            //若为黑节点，且至多有一个红色左子节点
            else {

                // 如果指定删除的节点 带有红色左子节点
                /*
                 *         |
                 *       cur(B)
                 *      /    \
                 *  left(R)  nil
                 */
                if(cur.left != null){
                    Node left = cur.left;
                    cur.left = null;

                    cur.key = left.key;
                    cur.value = left.value;

                    left = null;
                }
                // 如果删除的节点为叶子节点，即情况四(2-Node)
                /*
                 *      |        special
                 *    cur(B)    -------->     root
                 *    /   \                   /  \
                 *  nil   nil               nil  nil
                 */
                else{

                    // 如果该节点为根节点
                    if(cur == root){
                        root = null;
                        cur = null;
                    }
                    // 如果该节点为黑色叶子节点即（2-Node）
                    else{
                        fixAfterRemove(cur);
                        Node parent = cur.parent;

                        if(parent.left == cur) parent.left = null;
                        else parent.right = null;

                        cur = null;
                    }
                }

            }

        }


        size --;

        return oldValue;
    }


    // 删除后调整
    private void fixAfterRemove(Node node) {

        while(node != root){
            // 左
            if(node.parent.left == node){

                // 1. 兄弟节点为 red（说明它不是 2-3-4 树里面 node 的兄弟节点），需要通过旋转找到其 真正的兄弟节点（黑色）
                /*
                 *        p(B)                      r(B)
                 *       /    \                    /   \
                 *   node(B)  r(R)     -->       p(R)  rr(B)
                 *           /  \               /  \
                 *        rl(B) rr(B)      node(B) rl(B)
                 */
                if(node.left == null && node.right == null && node.parent.right.color == Node.RED){

                    Node parent = node.parent;
                    Node right = parent.right;
                    parent.color = Node.RED;
                    right.color = Node.BLACK;
                    leftRotate(parent);

                }

                // 2. 兄弟节点为真正的兄弟节点（黑色）
                // 情况 1 ：兄弟节点不为 2-Node，可借一个或两个节点过来
                // 情况 1.1 ：兄弟节点为 3-Node
                /*
                 *       p(c)                 p(c)
                 *      /   \                /    \
                 *  node(B) bro(B)  -->  node(B)  bl(B)
                 *          /                      \
                 *        bl(R)                   bro(R)
                 *                           |
                 *                           |
                 *                           V
                 *
                 *                          p(c)                  bro(c)
                 *                         /   \                  /   \
                 *                    node(B)  bro(B)   --->    p(B)  br(B)
                 *                               \             -/-
                 *                              br(R)        node(B)
                 */
                Node nodeBro = node.parent.right;
                if(node.left == null && node.right == null && nodeBro.right == null && nodeBro.left != null){

                    Node left = nodeBro.left;
                    left.color = Node.BLACK;
                    nodeBro.color = Node.RED;

                    rightRotate(nodeBro);

                }

                nodeBro = node.parent.right;
                if(node.left == null && node.right == null && nodeBro.right != null && nodeBro.left == null){

                    Node parent = node.parent;
                    nodeBro.color = parent.color;
                    nodeBro.right.color = Node.BLACK;
                    parent.color = Node.BLACK;

                    leftRotate(parent);


                    node = root; //只是为了退出循环
                }
                // 情况 1.2 ：兄弟节点为 4-Node, 由于兄弟节点为黑色节点，所以其子节点必为红节点（由黑高推出）
                /*
                 *        p(c)                      bro(c)
                 *       /   \                      /   \
                 *   node(B) bro(B)     -->      p(B)   br(B)
                 *           /  \              -/-  \
                 *        bl(R) br(R)       node(B) bl(R)
                 */
                else if (node.left == null && node.right == null && nodeBro.left != null && nodeBro.right != null){

                    Node parent = node.parent;
                    Node br = nodeBro.right;

                    nodeBro.color = parent.color;
                    parent.color = Node.BLACK;
                    br.color = Node.BLACK;

                    leftRotate(parent);


                    node = root; // 只是为了退出循环

                }
                // 情况 2：兄弟节点为 2-Node, 借不了，兄弟就变红降级，再向上回溯降级
                /*
                 *        p(c)
                 *      -/-  \
                 *   node(B) bro(B)
                 *
                 *  1) p.color == Node.RED ==> p.right = Node.RED; p.color = Node.BLACK; break !
                 *  2) p.color == Node.BLACK ==> p.right = Node.RED; p.color = Node.BLACK; 向上回溯
                 *
                 */
                else {

                    Node parent = node.parent;


                    nodeBro.color = Node.RED; //兄弟降级
                    if (parent.color == Node.RED){
                        parent.color = Node.BLACK;
                        break;
                    }


                    node = parent;  // 向上回溯降级,降级时 node 有时就不为叶子节点了，所以情况 1 中判断条件必须加上 node.left == null && node.right == null  (this is important !!!)

                }

            }
            // 右
            else{

                if(node.left == null && node.right == null && node.parent.left.color == Node.RED){

                    Node parent = node.parent;
                    Node left = parent.left;
                    parent.color = Node.RED;
                    left.color = Node.BLACK;
                    rightRotate(parent);

                }


                Node nodeBro = node.parent.left;
                if(node.left == null && node.right == null && nodeBro.left == null && nodeBro.right != null){

                    Node right = nodeBro.right;
                    right.color = Node.BLACK;
                    nodeBro.color = Node.RED;

                    leftRotate(nodeBro);

                }

                nodeBro = node.parent.left;
                if(node.left == null && node.right == null && nodeBro.left != null && nodeBro.right == null){

                    Node parent = node.parent;
                    nodeBro.color = parent.color;
                    nodeBro.left.color = Node.BLACK;
                    parent.color = Node.BLACK;

                    rightRotate(parent);


                    node = root; //只是为了退出循环
                }
                else if (node.left == null && node.right == null && nodeBro.right != null && nodeBro.left != null){

                    Node parent = node.parent;
                    Node bl = nodeBro.left;

                    nodeBro.color = parent.color;
                    parent.color = Node.BLACK;
                    bl.color = Node.BLACK;

                    rightRotate(parent);


                    node = root; // 只是为了退出循环

                }
                else {

                    Node parent = node.parent;


                    nodeBro.color = Node.RED; //兄弟降级
                    if (parent.color == Node.RED){
                        parent.color = Node.BLACK;
                        break;
                    }

                    node = parent;  // 向上回溯降级,降级时 node 有时就不为叶子节点了，所以情况 1 中判断条件必须加上 node.left == null && node.right == null  (this is important !!!)

                }

            }

        }


    }





}

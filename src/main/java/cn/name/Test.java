package cn.name;

/**
 * SINA：刘岩松
 * 2020-12-26
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println( "720*" + clacAspectRatio( 720, 1280, 720, false ) );
//        System.out.println( "1280*" + clacAspectRatio( 720, 1280, 1280, true ) );
//        System.out.println( "2880*" + clacAspectRatio( 1080, 1920, 2880, false ) );
//        System.out.println( "1440*" + clacAspectRatio( 1080, 1920, 1440, true ) );

        int weight = 720;
        int height = 1170;
        int divisor = 0;
        for(int i = 99;i > 1;i--){
            if((weight + height) % i == 0){
                System.out.println(i);
                divisor = i;
                break;
//                System.out.println("a:"+a/i);
//                System.out.println("b:"+b/i);
            }
//            System.out.println("============");
        }

        for(int i =99 ; i > 1 ; i--){
            if((height - divisor * i) > weight / 1.524){
                System.out.println("倍数："+i);
                System.out.println("剩余高度："+ (height - divisor * i));
//                break;
            }
        }

        System.out.println((int) 99 * 7 / 2);

    }



    /**
     * 计算长宽比
     *
     * 先计算原始长宽比(目标宽高/原始宽高)，在乘上原始宽高，即：
     * 求高 -> 原始高 × ( 目标宽 ÷ 原始宽 )
     * 求宽 -> 原始宽 × ( 目标高 ÷ 原始高 )
     *
     * @param fromWidth         原始宽度
     * @param fromHeight        原始高度
     * @param toWidthOrHeight   目标宽度或高度
     * @param isWidth           <code>toWidthOrHeight</code>是宽度还是高度
     * @return                  <code>isWidth</code>为true则返回结果为高度，否则为宽度
     */
    public static int clacAspectRatio(int fromWidth, int fromHeight,
                               int toWidthOrHeight, boolean isWidth) {
        if( isWidth ) {
            return (int) ((double) fromHeight * ( (double) toWidthOrHeight / (double) fromWidth ));
        }else {
            return (int) ((double) fromWidth * ( (double) toWidthOrHeight / (double) fromHeight ));
        }
    }
}

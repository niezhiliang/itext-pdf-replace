### itext7 pdf关键字替换

不建议使用{}来包裹关键字，因为如果需要替换的关键字下面有下划线的话，{}的占位会到下划线的下面，造成在替换的时候遮盖下划线,所以改用<>来代替{}

还有部分字母是不能使用的 跟{}原理一样，占位会到下划线的下面。不能使用一些字母`q`、`y`、`p`、`g`、`j`、`Q`

在使用白色背景覆盖原本的字体时，y坐标需要+1.5f 替换的时候字体需要-1.5f这样就替换就能达到比较好的效果



- 源文件

![源文件](https://github.com/niezhiliang/itext-pdf-replace/blob/master/data/1.png)


- 替换后的文件
![源文件](https://github.com/niezhiliang/itext-pdf-replace/blob/master/data/2.png)

这是第一个关于设计模式的笔记[^1]:snail:

# Adapter
适配器模式，是指将一个接口转变为另一个所需的接口的模式。注意此处接口并非只是指 Java 中的 interface，也指些利用多态时的超类型！

![image](https://user-images.githubusercontent.com/59677595/173985149-86d0c61e-d8a7-4eea-ab1f-eb7e177e5a36.png)


适配器模式有两种实现方法：
- 类适配器
  
  利用**继承而且是多重继承**，在 java 中并不支持。
  
  ![image](https://user-images.githubusercontent.com/59677595/173987002-48732ba6-6752-4341-95cc-5f07d69761f3.png)

  一大优点是可以不用专门实现一个被适配者的类实例，直接实现被适配者的方法。必要时可以覆盖被适配者的接口。

- 对象适配器

  利用**组合**实现
  
  ![image](https://user-images.githubusercontent.com/59677595/173986673-0af37136-e814-4b63-a712-585a11b77799.png)
  
  一大优点就是 java 支持其实现，可以组合多个被适配者。
  


# Facade
外观模式，将一组操作或者方法调用接口简化为一个简洁的接口，即高层接口，以便客户调用。实质是对封装思想的体现。这种模式不代表将底层接口完全向外界屏蔽，如果想要调用底层，
依然可以直接调用。

![image](https://user-images.githubusercontent.com/59677595/173988621-cacf8aa0-2f10-4124-877f-d30b973867ef.png)

这种封装会导致很多类的相互依赖，为了避免太多依赖难以维护，提出了一个"最少知识"法则：

在对象方法内，只允许调用如下范围的方法：
- 方法参数中的对象
- 方法中创建的对象
- 对象本身
- 对象拥有的组件

```java
// This is a bad practice
Class A{
  
  Station station;
  
  public float getTemp(){
      return station.getThermometer().getTemperature();   //不符法则，这里调用了规定范围以外的方法 getTemperature()
  } 
}

//This is a better one  改进后
Class A{
  
  Station station;
  
  public float getTemp(){
      Thermometer thermometer = station.getThermometer();
      return getTemp(thermometer);
  }
  
  public float getTemp(Thermometer thermometer){
      return thermometer..getTemperature();
  }
  
  
}

```

很明显，这个规则缺点就是增加了开发的代码复杂度，有时候遵从这个规则仿佛没有很大的意义:thinking:
[^1]: 代码部分在 src 目录下

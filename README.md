# genSets
idea plugin for generate variable set methods

```
class Foo{
  String name;
  Integer age;
  String bar;
  String test;
  String like;
}

```

```
Foo foo = new Foo();
foo.allSet
```


```
Foo foo = new Foo();
foo.setName();
foo.setAge();
foo.setBar();
foo.setTest();
foo.setLike();
```

针对Spring JPA的Model、DTO转换功能生成代码片段
```
使用方法：

        // 输入 dto.todto
        dto.setBusinessCode(model.getBusinessCode());
        dto.setPath(model.getPath());
        dto.setName(model.getName());
        dto.setType(model.getType());
        dto.setSize(model.getSize());
        dto.setWebCustomerContacts(model.getWebCustomerContacts());
        dto.setFinished(model.isFinished());
        dto.setAllFinished(model.getAllFinished());

        this.convertCommonToDTO(model, dto);


        // 输入 model.tomodel
        model.setCustomer(dto.getCustomer());
        model.setPath(dto.getPath());
        model.setName(dto.getName());
        model.setType(dto.getType());
        model.setSize(dto.getSize());
```
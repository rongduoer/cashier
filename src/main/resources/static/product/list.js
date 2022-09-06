var xhr = new XMLHttpRequest()
xhr.open('get','/product/list.json')
//成功的时候
xhr.onload = function () {
    console.log(this, status)
    console.log(this.responseText)

    //进行JSON字符串 -> JS对象
    var ret = JSON.parse(this.responseText)
    // 根据是否有 redirectUrl 来判断是正确还是错误情况
    if (ret.redirectUrl) {
        // 返回里有 redirectUrl，说明错了，由 JS 进行页面的跳转
        // JS 提供了一个方法，让页面跳转到新的地方
        location.assign(ret.redirectUrl)  // 到时候的跳转是我们通过 JS 自己完成的，不是 HTTP 层面上的重定向（浏览器自动完成）
    }

    var productList = ret.data;
    //元素定位
    var oTbody = document.querySelector('tbody')
    // 通过遍历 productList，得到每一个 商品，根据商品中的值，修改 DOM 结构（为 <tbody> 添加一个 <tr> 一行
    for (var product of productList) {
        var html = "<tr>" +
            `<td>${product.name}</td>` +  // 反引号，模板字符串
            `<td>${product.introduce}</td>` +
            `<td>${product.stock}</td>` +
            `<td>${product.unit}</td>` +
            `<td>${product.price}</td>` +
            `<td>${product.discount}</td>` +
            "</tr>";

        // 添加到 tbody 的内部
        oTbody.innerHTML += html
    }
}

xhr.send()

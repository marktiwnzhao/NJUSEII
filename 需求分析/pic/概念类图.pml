@startuml
left to right direction
class "客户" as customer {
    客户编号
    __
    姓名
    __
    联系方式
    __
    身份证号
    __
    住址
    __
    积分
    __
    信用
}
class "铁路管理员" as admin {
    编号
    __
    姓名
    __
    联系方式
    __
    工号
}
class "车票" as ticket {
    车票号
    __
    出发地
    __
    目的地
    __
    发车时间
    __
    列车号
    __
    车次
    __
    座位
}
class "订单" as order {
    订单号
    __
    总价
    __
    支付方式
    __
    完成时间
    __
    购票人姓名
    __
    购票人联系方式
    __
    车票
}
class "铁路信息" as railway {
    始发站
    __
    终点站
    __
    列车号
    __
    车次
}
class "购买车票清单" as buy_tickets_list {
}
class "总车票清单" as all_tickets_list {
    出发地
    __
    目的地
    __
    出行日期
}
class "购买信息" as buy{
    时间
    __
    用户编号
}
customer "1" -- "*" buy : 浏览并购买余票
buy_tickets_list "1" o-- "0..*" ticket 
all_tickets_list "1" o-- "0..*" ticket 
buy "1" o-- "1" order 
buy "1" o-- "1" buy_tickets_list 
railway "1" --* "1" ticket
admin "1" -- "1" all_tickets_list : 管理
admin "1" -- "1" railway : 管理
customer "1" -- "1" all_tickets_list : 检索查看

@endum
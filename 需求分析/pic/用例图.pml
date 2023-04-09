@startuml
left to right direction
actor "客户" as a
actor "铁路管理员" as b
package "铁路在线订票系统" {
    usecase "个人信息管理" as UC1
    usecase "浏览车票详细信息" as UC2
    usecase "购票" as UC3
    usecase "铁路信息管理" as UC4
    usecase "车票信息管理" as UC5
}
a-->UC1
a-->UC2
a-->UC3
b-->UC4
b-->UC5
@enduml
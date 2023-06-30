<script setup lang="ts">

import { request } from "~/utils/request";
import { ElNotification } from "element-plus";
import {h, onMounted, reactive, ref, watch} from "vue";
import { useStationsStore } from "~/stores/stations";
import { parseDate } from "~/utils/date";
import { useRouter } from "vue-router";
import { OrderDetailData } from "~/utils/interfaces";

const router = useRouter()
const stations = useStationsStore()

const selectStrategyFormVisible = ref(false);
const selectStrategyForm = ref({
    strategy: 0
});
const payStrategies = ref([{
    id: 0,
    name: '支付宝支付'
}, {
    id: 1,
    name: '微信支付'
}])

const props = defineProps({
  id: Number,
})

let orderDetail = reactive<{ data: OrderDetailData }>({
  data: {
    id: 0,
    train_id: 0,
    seat: '',
    status: '',
    created_at: '',
    start_station_id: 0,
    end_station_id: 0,
    departure_time: '',
    arrival_time: '',
    money: 0,
    point: 0,
    raw_money:0,
    raw_point: 0,
    used_point: 0
  },
})

let train = reactive<{ data: { name?: string } }>({
  data: {}
});

const getOrderDetail = () => {
  request({
    url: `/order/${props.id}`,
    method: 'GET',
  }).then(res => {
    orderDetail.data = res.data.data
    console.log(orderDetail.data)
  }).catch(err => {
    console.log(err)
    if (err.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: 'getOrder错误',
      message: h('i', { style: 'color: teal' }, err.response?.data.msg),
    })
  })
}

const getTrain = () => {
  console.log("getTrain")
  if (orderDetail.data) {
    request({
      url: `/train/${orderDetail.data.train_id}`,
      method: 'GET'
    }).then((res) => {
      train.data = res.data.data
      console.log(train)
    }).catch((error) => {
      ElNotification({
        offset: 70,
        title: 'getTrain错误(orderDetail)',
        message: h('error', { style: 'color: teal' }, error.response?.data.msg),
      })
      console.log(error)
    })
  }
}


const pay = (id: number) => {
  request({
    url: `/order/${id}`,
    method: 'PATCH',
    data: {
      status: '已支付'
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '支付成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    getOrderDetail()
    console.log(res)
  }).catch((error) => {
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: '支付失败',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
    console.log(error)
  })
}

const selectPayStrategy = (id : number, strategy : number) => {
  request({
      url: `/order/strategy`,
      method: 'PATCH',
      params: {
          id: id,
          strategy: strategy
      }
  }).then((res) => {
      pay(id);
      console.log(res);
  }).catch((error) => {
      if (error.response?.data.code == 100003) {
          router.push('/login');
      }
      ElNotification({
          offset: 70,
          title: '选择支付策略失败',
          message: h('error', { style: 'color: teal' }, error.response?.data.msg),
      });
      console.log(error);
  })
}

const cancel = (id: number) => {
  request({
    url: `/order/${id}`,
    method: 'PATCH',
    data: {
      status: '已取消'
    }
  }).then((res) => {
    ElNotification({
      offset: 70,
      title: '取消成功',
      message: h('success', { style: 'color: teal' }, res.data.msg),
    })
    getOrderDetail()
    console.log(res)
  }).catch((error) => {
    if (error.response?.data.code == 100003) {
      router.push('/login')
    }
    ElNotification({
      offset: 70,
      title: '取消失败',
      message: h('error', { style: 'color: teal' }, error.response?.data.msg),
    })
    console.log(error)
  })
}

watch(orderDetail, () => {
  getTrain()
})

onMounted(() => {
  stations.fetch()
  getOrderDetail()
})

getOrderDetail()

</script>

<template>
  <div style="display: flex; flex-direction: column">

    <div style="margin-bottom: 2vh;">
      <el-button style="float:right" @click="getOrderDetail">
        刷新
      </el-button>
    </div>

    <div style="display: flex; justify-content: space-between;">
      <div>
        <el-text size="large" tag="b" type="primary">
          订单号:&nbsp;&nbsp;
        </el-text>
        <el-text size="large" tag="b">
          {{ props.id }}
        </el-text>
      </div>
      <div>
        <el-text size="large" tag="b" type="primary">
          创建日期:&nbsp;&nbsp;
        </el-text>
        <el-text size="large" tag="b" v-if="orderDetail.data">
          {{ parseDate(orderDetail.data.created_at) }}
        </el-text>
      </div>
    </div>

    <div>
      <el-text size="large" tag="b" type="primary">
        订单状态:&nbsp;&nbsp;
      </el-text>
      <el-text size="large" tag="b" v-if="orderDetail.data">
        {{ orderDetail.data.status }}
      </el-text>
    </div>
    <div style="margin-bottom: 2vh">
      <el-text size="large" tag="b" type="primary">
        车次信息:
      </el-text>
    </div>

    <el-descriptions :column="4" border>
      <el-descriptions-item :span="2" width="25%" align="center">
        <template #label>
          <el-text type="primary" tag="b" size="large">
            车次
          </el-text>
        </template>
        <el-text type="primary" tag="b" size="large">
          {{ train?.data?.name }}
        </el-text>
      </el-descriptions-item>
      <el-descriptions-item label="席位信息" :span="2" width="25%" align="center" v-if="orderDetail.data">
        {{ orderDetail.data.seat }}
      </el-descriptions-item>
      <el-descriptions-item label="出发站" :span="2" width="25%" align="center" v-if="orderDetail.data">
        {{ stations.idToName[orderDetail.data.start_station_id] ?? '未知站点' }}
      </el-descriptions-item>
      <el-descriptions-item label="到达站" :span="2" width="25%" align="center" v-if="orderDetail.data">
        {{ stations.idToName[orderDetail.data.end_station_id] ?? '未知站点' }}
      </el-descriptions-item>
      <el-descriptions-item label="出发时间" :span="2" width="25%" align="center" v-if="orderDetail.data">
        {{ parseDate(orderDetail.data.departure_time) }}
      </el-descriptions-item>
      <el-descriptions-item label="到达时间" :span="2" width="25%" align="center" v-if="orderDetail.data">
        {{ parseDate(orderDetail.data.arrival_time) }}
      </el-descriptions-item>
      <el-descriptions-item label="金额" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ orderDetail.data.money }}
      </el-descriptions-item>

      <el-descriptions-item label="得到的积分" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ orderDetail.data.point }}
      </el-descriptions-item>

      <el-descriptions-item label="原金额" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ orderDetail.data.raw_money }}
      </el-descriptions-item>

      <el-descriptions-item label="原积分" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ orderDetail.data.raw_point }}
      </el-descriptions-item>

      <el-descriptions-item label="使用积分" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ orderDetail.data.used_point }}
      </el-descriptions-item>

      <el-descriptions-item label="折扣金额" :span="2" width="25%" align="center" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
        {{ (orderDetail.data.raw_money - orderDetail.data.money).toFixed(2) }}
      </el-descriptions-item>

    </el-descriptions>

    <div style="margin-top: 2vh" v-if="orderDetail.data && orderDetail.data.status === '等待支付'">
      <div style="float:right;">
        <el-button type="danger" @click="cancel(id ?? -1)">
          取消订单
        </el-button>
        <el-button type="primary" @click="selectStrategyFormVisible = true">
          支付订单
        </el-button>

        <el-dialog v-model="selectStrategyFormVisible" title="请选择支付方式">
          <el-form :model="selectStrategyForm">
            <el-form-item label="支付方式" prop="strategy">
              <el-select v-model="selectStrategyForm.strategy" placeholder="请选择支付方式">
                <el-option
                  v-for="item in payStrategies"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="selectStrategyFormVisible = false">取 消</el-button>
            <el-button type="primary" @click="selectPayStrategy(id ?? -1, selectStrategyForm.strategy)">确 定</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
    <div v-else-if="orderDetail.data && orderDetail.data.status === '已支付'" style="margin-top: 2vh">
      <div style="float:right;">
        <el-button @click="cancel(id ?? -1)">
          取消订单
        </el-button>
      </div>
    </div>

  </div>
</template>

<style scoped></style>
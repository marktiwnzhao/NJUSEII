<script setup lang="ts">

import { useRoute } from "vue-router";

const route = useRoute()
const hasOrder = typeof route.params['orderId'] === 'string';

</script>

<template>
  <el-container>
    <el-header style="position: fixed; width: 100%; z-index: 999">
      <MenuComponent pageIndex="/order" />
    </el-header>
    <el-main style="margin-top: 10vh">
      <div v-if="hasOrder">
        <el-text size="large" type="primary" style="display: flex;justify-content: center">
          <h1>订单详情</h1>
        </el-text>
        <br />


      <div style="display: flex; justify-content: center; margin-top: 5vh">
        <OrderDetail :id="typeof $route.params['orderId'] === 'string' ? parseInt($route.params['orderId']) : undefined"
          style="width: 50%" />
      </div>
      </div>
      <div v-else>
        <el-text size="medium" type="info" style="display: flex; justify-content: center">
          <h3>当前没有订单</h3>
        </el-text>
      </div>
    </el-main>
  </el-container>
</template>

<style scoped></style>
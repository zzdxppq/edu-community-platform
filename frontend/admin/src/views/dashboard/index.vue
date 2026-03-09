<template>
  <div class="dashboard-page">
    <h2>工作台</h2>
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>用户总数</template>
          <div class="dashboard-stat">0</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>示范校数</template>
          <div class="dashboard-stat">0</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>政策文件</template>
          <div class="dashboard-stat">0</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>资源总量</template>
          <div class="dashboard-stat">0</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard-stat {
  font-size: 32px;
  font-weight: 700;
  color: #2C5AA0;
  text-align: center;
}
</style>

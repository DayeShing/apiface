/**
 * 空白数据页
 * author:Daye Shing
 */
<template>
  <div class="await-mian">
    <div class="top_div"></div>
    <div class="tip-card">
      <div>
        <div class="tou"></div>
        <div class="initial_left_hand"></div>
        <div class="initial_right_hand"></div>
      </div>
      {{ $t("page.blank") }}
    </div>
    <div class="tip-foot">{{ $t("page.v.self") }}</div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less">
.await-mian {
  width: 100%;
  height: 100%;
  background: #ebebeb;
  font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei",
    "\9ED1\4F53", Arial, sans-serif;
  color: #666;
  position: relative;
  font-size: 12px;
  .top_div {
    background: #008ead;
    width: 100%;
    height: 45%;
  }
  .tip-card {
    border-radius: 4px;
    font-size: 32px;
    width: 400px;
    line-height: 200px;
    margin: auto auto;
    background: #ffffff;
    text-align: center;
    margin-top: -100px;
    border: 1px solid #e7e7e7;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    & > div {
      width: 165px;
      position: absolute;
      .tou {
        background: url(../images/tou.png) no-repeat;
        width: 97px;
        height: 92px;
        position: absolute;
        top: -87px;
        left: 140px;
      }
      .initial_left_hand {
        background: url(../images/hand.png) no-repeat;
        width: 30px;
        height: 20px;
        position: absolute;
        top: -12px;
        left: 100px;
      }
      .initial_right_hand {
        background: url(../images/hand.png) no-repeat;
        width: 30px;
        height: 20px;
        position: absolute;
        top: -12px;
        right: -112px;
      }
    }
  }
  .tip-foot {
    position: absolute;
    bottom: 30px;
    text-align: center;
    width: 100%;
  }
}
</style>

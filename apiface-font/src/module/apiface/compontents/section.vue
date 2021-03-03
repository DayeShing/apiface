<!--
  封装收缩框
  作者：Daye Shing
-->
<template>
  <fieldset class="fieldset-box">
    <legend>
      <span>{{ label }}</span>
      <i
        class="fa"
        :class="{
          'fa-chevron-down': isCollapsed,
          'fa-chevron-up': !isCollapsed,
        }"
        @click.prevent="collapse"
      ></i>
    </legend>
    <div class="collapsible" :class="{ hidden: collapsed }">
      <slot />
    </div>
  </fieldset>
</template>
<script>
export default {
  data() {
    return {
      isCollapsed: false,
    };
  },
  props: {
    label: {
      type: String,
      default: "Section",
    },
    collapsed: {
      type: Boolean,
    },
  },
  methods: {
    //展开或者折叠
    collapse({ target }) {
      const parent = target.parentNode.parentNode;
      parent.querySelector(".collapsible").classList.toggle("hidden");
      this.isCollapsed = !this.isCollapsed;
    },
  },
};
</script>

<style lang="less">
.fieldset-box {
  padding: 5px 12px 12px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;

  legend {
    display: inline-block;
    align-items: center;
    justify-content: center;
    color: #bcc2cd;
    font-weight: 800;
    padding-inline-start: 5px;
    padding-inline-end: 5px;
    padding-bottom: 3px;
    * {
      vertical-align: middle;
    }
    i {
      cursor: pointer;
      padding-left: 3px;
    }
  }
  &.purple {
    border: 1px solid #c1aae1;
    legend {
      color: #bd93f9;
    }
  }
  &.orange {
    border: 1px solid #eecba6;
    legend {
      color: #ffb86c;
    }
  }
  &.pink {
    border: 1px solid #f6badd;
    legend {
      color: #ff79c6;
    }
  }
  &.blue {
    border: 1px solid #a7daf8;
    legend {
      color: #57b5f9;
    }
  }
  .collapsible.hidden {
    display: none;
  }
}
</style>

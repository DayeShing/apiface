<!--
  菜单
  作者：Daye Shing++#062a31
  需要格式：{label:"",key:'',child:[{label:'',key:''}]}
-->
<template>
  <div class="self-menu">
    <div class="search-menu">
      <el-input
        :placeholder="$self('search')"
        prefix-icon="el-icon-search"
        @change="search"
        v-model="keyword"
      ></el-input>
    </div>
    <div class="self-menu__inner">
      <el-menu
        :default-active="path"
        background-color="#545c64"
        text-color="#B2B8C2"
        active-text-color="#fff"
        :default-openeds="openeds"
        @select="menuSelect"
      >
        <div class="no-menu" v-if="menus.length == 0">{{ $self("nodata") }}</div>
        <el-submenu :index="item.key" v-for="item in menus" :key="item.label">
          <template slot="title">
            <i class="el-icon-collection" />
            {{ item.label }}
          </template>
          <el-menu-item
            :index="item.key + '-' + it.key"
            v-for="it in item.child"
            :key="item.key + '-' + it.key"
          >
            {{ it.label[0]||it.path }}
            <el-tooltip effect="light" placement="right" :open-delay="500">
              <div slot="content">
                <span>API : {{ it.path }}</span>
              </div>
              <i class="fa fa-info-circle info-btn"></i>
            </el-tooltip>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      path: "",
      keyword: "",
      oldActive: "",
      all: [],
      menus: [],
      searchFlag: false,
      openeds: [],
      page: "menu."
    };
  },
  methods: {
    $self(k) {
      return this.$t(this.page + k);
    },
    loadMenus(group, path, menus) {
      this.menus = menus;
      this.path = group + "-" + path;
      this.oldActive = "";
      this.$emit("selectMenu", group, path);
    },
    search(value) {
      if (!this.searchFlag) {
        this.all = this.menus;
      }
      this.openeds = [];
      this.menus = [];
      // 在搜索
      if (value != "") {
        this.searchFlag = true;
        for (var i = 0; i < this.all.length; i++) {
          if (this.all[i].label.indexOf(value) != -1) {
            this.menus.push(this.all[i]);
            this.openeds.push(this.all[i].key);
          } else {
            var top = { label: this.all[i].label, key: this.all[i].key };
            var tmp = [];
            for (var j = 0; j < this.all[i].child.length; j++) {
              for (var k = 0; k < this.all[i].child[j].label.length; k++) {
                if (this.all[i].child[j].label[k].indexOf(value) != -1) {
                  tmp.push(this.all[i].child[j]);
                  break;
                }
              }
            }
            if (tmp.length > 0) {
              top.child = tmp;
              this.menus.push(top);
              this.openeds.push(top.key);
            }
          }
        }
      } else {
        // 清空搜索
        this.menus = this.all;
        this.all = [];
        this.searchFlag = false;
      }
      this.change();
    },
    change() {
      if (this.menus.length > 0) {
        var paths = [this.menus[0].key, this.menus[0].child[0].key];
        this.path = paths[0] + "-" + paths[1];
        if (this.oldActive != this.path) {
          this.oldActive = this.path;
          this.$emit("selectMenu", paths[0], paths[1]);
        }
      }
    },
    //选择菜单后抛出事件
    menuSelect(key, keyPath) {
      if (this.oldActive != key) {
        this.oldActive = key;
        var paths = key.split("-", 2);
        this.$emit("selectMenu", paths[0], paths[1]);
      }
    }
  }
};
</script>

<style lang="less">
//
.self-menu {
  height: 100%;
  position: relative;
  padding-top: 43px;
  background: #545c64;
  .scrollbar-wrapper {
    overflow-x: hidden !important;
    .el-scrollbar__view {
      height: 100%;
    }
  }
  .no-menu {
    padding-top: 300px;
    color: #e2e6ec;
    text-align: center;
  }
  .search-menu {
    top: 0px;
    position: absolute;
    width: 100%;

    padding: 4px 12px;
    .el-input__prefix {
      font-size: 14px !important;
      left: 2px !important;
      color: #e2e6ec !important;
    }
    .el-input--small,
    .el-input--small .el-input__inner {
      height: 35px !important;
      line-height: 35px !important;
      vertical-align: top;
    }
    input[type="text"] {
      background: #545c64;
      border: 0;
      color: #fff;
    }
  }
  .self-menu__inner {
    height: 100%;
    overflow-y: auto;
    &::-webkit-scrollbar {
      width: 0px !important;
    }

    .el-menu {
      width: 99%;
      min-height: 100%;
      border-right: solid 0px #e6e6e6 !important;
      .el-submenu [class^="el-icon-"] {
        margin-right: 0px !important;
        margin-left: 5px;
      }
      .el-submenu .el-menu-item {
        padding-right: 35px !important;
        padding-left: 32px !important;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        position: relative;
        &.is-active,&:hover {
          .info-btn {
            display: initial !important;
          }
        }
        .info-btn {
          display: none;
          position: absolute;
          right: 10px;
          top: 14px;
          &:hover {
            color: aliceblue;
          }
        }
      }
      .el-menu-item-group__title,
      .el-menu-item,
      .el-submenu__title {
        font-size: 14px !important;
        height: 38px !important;
        line-height: 38px !important;
        padding: 0px !important;
        .el-submenu__icon-arrow {
          right: 12px !important;
        }
      }
      .el-menu-item.is-active {
        background-color: rgb(67, 74, 80) !important;
      }
    }
  }
}
</style>

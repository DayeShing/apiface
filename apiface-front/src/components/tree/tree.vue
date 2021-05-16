<template>
  <div class="u-tree" v-bind:class=" [config.backgroud,selectDisableClass] ">
    <div class="div-search">
      <input
        type="text"
        class="input-search"
        :class="{'input-search-width': keyword!='' }"
        v-model="keyword"
        @focus="placeholderFlag = false"
        @blur="blur"
        @keyup.enter="doSearch"
      />
      <label for="search" v-show="placeholderFlag">搜索关键字</label>
      <div class="tree-search-btn">
        <el-button
          v-show="keyword!=''"
          type="primary"
          icon="el-icon-delete"
          size="mini"
          class="btn"
          @click.native="clear"
        ></el-button>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          class="btn"
          @click.native="doSearch"
        ></el-button>
      </div>
    </div>
    <div
      class="tree_content"
      v-loading="loading"
      element-loading-spinner="fa fa-spinner fa-pulse fa-3x"
    >
      <slot name="header"></slot>
      <u-scrollbar class="tree_warp" @contextmenu.prevent.native="onContextmenu($event)">
        <el-tree
          ref="filterTree"
          :check-strictly="config.checkStrictly"
          :render-after-expand="false"
          @check-change="checkChange"
          :show-checkbox="config.showCheckbox"
          class="filter-tree"
          :data="config.data"
          :load="loadNode"
          :props="defaultProps"
          node-key="id"
          lazy
          :highlight-current="config.highlightCurrent"
          :expand-on-click-node="config.expandOnClickNode"
          :filter-node-method="filterNode"
          @node-click="clickEvent"
          @mousedown.prevent.native="mousedownEvent"
          :render-content="renderContent"
        ></el-tree>
      </u-scrollbar>
    </div>
  </div>
</template>

<script type="es6">
import Scrollbar from "components/scrollbar";
import "./tree.less";
import Qs from "qs";
const icon = {
  dbtableIndex: "fa-folder-o",
  dbtable: "fa-database"
};
export default {
  data() {
    return {
      inited: false,
      searching: false,
      keyword: "",
      keyFlag: false,
      placeholderFlag: true,
      checkedNodes: [],
      // defaultProps: {
      //   children: "children",
      //   label: "text"
      // },
      config: {
        url: "", //请求地址
        data: [],
        isLoad: true,
        headerNode: [{ entryId: "0", text: "根目录", type: "index" }], //存在于根节点上面的node
        leafType: "", //叶子节点类型
        selectNodeType: "",
        icon: icon, //字段类型列表
        showRightMenu: false, //是否开启hover菜单
        expandOnClickNode: false,
        dragTag: false, //是否可以拖拽标签（当拖拽元素为叶子节点类型）
        dropTarget: [], //拖拽目标栏target
        highlightCurrent: true,
        includeRootNode: false, //包含根节点
        backgroud: "white", //主题颜色，有一种是为了区分下拉框的
        showCheckbox: false,
        checkStrictly: false,
        showCheckboxNodeType: "", //显示checkBodx的nodetype
        method: "post", //接收方式
        queryParams: {}, //参数列表
        loadNodeAfterSearch: false, //是否在搜索后还允许打开节点
        renderFunc: Function,
        iconGroup: {
          edit: false,
          add: false,
          delete: false
        }
        // lazy:true,//url或者此参数为false，就搜本地

        // doSearchFlag:false,
      }
    };
  },
  props: {
    tree: Object
  },
  mounted() {},
  components: { "u-scrollbar": Scrollbar },
  computed: {
    defaultProps() {
      var self = this;
      return {
        children: "children",
        label: "text",
        disabled: function(data, node) {
          if (
            self.config.showCheckbox &&
            self.config.showCheckboxNodeType !== data.type
          ) {
            return true;
          }
          return false;
        }
      };
    },
    selectDisableClass() {
      if (
        this.config.showCheckbox &&
        this.config.showCheckboxNodeType !== "" &&
        this.config.checkStrictly
      ) {
        return "hiddenCheckBox";
      }
      return " ";
    },
    loading() {
      if (!this.inited) {
        return true;
      }
      if (this.searching) {
        return true;
      }
      return false;
    }
  },
  created() {
    this.setConfig(this.tree);
  },
  methods: {
    /**设置config */
    setConfig: function(params) {
      if (params === undefined) return;
      for (var i in params) {
        if (params.hasOwnProperty(i)) {
          this.config[i] = params[i];
        }
      }
    },
    /**清空搜索,还原树 */
    clear: function() {
      this.keyword = "";
      this.placeholderFlag = true;
      this.doSearch();
    },
    /**搜索框失去找点时候 */
    blur: function() {
      this.keyword == ""
        ? (this.placeholderFlag = true)
        : (this.placeholderFlag = false);
    },
    /**右击事件  */
    onContextmenu: function(event) {
      var node = {};
      let $this = this;
      if (
        event.target === this.$el.getElementsByClassName("tree-box")[0] ||
        event.target ===
          this.$el.getElementsByClassName("el-tree__empty-block")[0] ||
        event.target ===
          this.$el.getElementsByClassName("el-tree__empty-text")[0] ||
        event.target ===
          this.$el.getElementsByClassName("el-scrollbar__wrap")[0] ||
        event.target === this.$el.getElementsByClassName("is-horizontal")[0] ||
        event.target === this.$el.getElementsByClassName("is-vertical")[0]
      ) {
        node = null;
      } else {
        event.target.click();
        let result = $this.parents(event.target, "el-tree-node");
        if (result != false) {
          let nodeDom = result.getElementsByClassName("tree-node-id")[0],
            nodeId = nodeDom.getAttribute("data-id"),
            nodePid = nodeDom.getAttribute("data-pid"),
            nodeType = nodeDom.getAttribute("data-type"),
            nodeText = nodeDom.getAttribute("data-text"),
            nodepText = nodeDom.getAttribute("data-pText");
          node = {
            id: nodeId,
            type: nodeType,
            pId: nodePid,
            pText: nodepText,
            text: nodeText
          };
        }
      }
      this.$emit("onRightClick", event, node);
    },
    /**非异步树搜索树节点方法 */
    filterNode(value, data) {
      if (!value) return true;
      return data.text.indexOf(value) !== -1;
    },
    /**开始搜索树 */
    doSearch() {
      let $this = this;
      $this.searching = true;
      $this.inited = true;
      if ($this.config.url === "") {
        this.$refs.filterTree.filter($this.keyword);
        $this.searching = false;
        return;
      }
      // if(!this.config.lazy){
      //     this.$refs.filterTree.$data.store.lazy = false;

      //   this.$refs.filterTree.filter($this.keyword);
      //   $this.searching = false;
      //   return;
      // }
      // if(this.config.showCheckbox){
      //   this.checkedNodes = this.getCheckedNodes();
      // }

      $this.config.queryParams["keyword"] = $this.keyword;
      if (
        !$this.config.queryParams["id"] ||
        $this.config.queryParams["id"] == ""
      ) {
        $this.config.queryParams["id"] = "0";
      }
      if ($this.config.method.toUpperCase() == "get".toUpperCase()) {
        $this
          .$catch($this.config.url, {
            params: $this.config.queryParams,
            timeout: 5000
          })
          .then(res => {
            $this.filterNodeEvent(res);
            if ($this.config.includeRootNode) {
              for (var i = 0; i < this.config.headerNode.length; i++) {
                res.data.unshift(this.config.headerNode[i]);
              }
            }
            $this.searching = false;
          })
          .catch(err => {
            $this.searching = false;
            $this.$message({ type: "error", message: "操作出错!" });
          });
      } else {
        $this
          .$fetch($this.config.url, Qs.stringify($this.config.queryParams))
          .then(res => {
            $this.filterNodeEvent(res);
            if ($this.config.includeRootNode) {
              for (var i = 0; i < this.config.headerNode.length; i++) {
                res.data.unshift(this.config.headerNode[i]);
              }
            }
            $this.searching = false;
          })
          .catch(err => {
            console.log(err);
            $this.searching = false;
            $this.$message({ type: "error", message: "操作出错!" });
          });
      }
    },
    /**过滤节点事件 */
    filterNodeEvent(res) {
      if (res && res.success == true) {
        if (this.keyword !== "") {
          if (this.config.loadNodeAfterSearch) {
            this.$refs.filterTree.$data.store.lazy = true;
            var tempArr = [];
            this.getDefaultExpandedKeys(res.data, tempArr);
            this.$refs.filterTree.$data.store.defaultExpandedKeys = tempArr;
          } else {
            this.$refs.filterTree.$data.store.defaultExpandedKeys = [];
            this.$refs.filterTree.$data.store.lazy = false;
          }
          this.config.data = res.data;
          this.keyFlag = true;
        } else {
          this.$refs.filterTree.$data.store.defaultExpandedKeys = [];
          this.$refs.filterTree.$data.store.lazy = true;
          this.config.data = res.data;
          this.keyFlag = false;
        }
        // this.$nextTick(()=>{
        //   if(this.config.showCheckbox){
        //     this.$refs.filterTree.setCheckedNodes(this.checkedNodes);
        //   }
        // })
      } else {
        this.messages(this, "error", res.code);
      }
    },
    /**当搜索异步树后是否还需要打开节点时 ---递归获取已得到的节点 */
    getDefaultExpandedKeys: function(data, arr) {
      var $this = this;
      for (var i in data) {
        if (data[i].children != undefined && data[i].children.length > 0) {
          arr.push(data[i].id);
          $this.getDefaultExpandedKeys(data[i].children, arr);
        }
      }
    },

    /**异步树加载数据方法 */
    loadNode(node, resolve) {
      if (!this.config.isLoad) {
        this.inited = true;
        this.config.isLoad = true;
        return;
      }
      if (
        this.$refs.filterTree !== undefined &&
        this.config.loadNodeAfterSearch
      ) {
        if (
          (node != null &&
            node.data != null &&
            node.data != undefined &&
            node.data.children != undefined &&
            node.data.children.length > 0) ||
          (this.$refs.filterTree.$data.store.defaultExpandedKeys !==
            undefined &&
            this.$refs.filterTree.$data.store.defaultExpandedKeys.indexOf(
              node.data.entryId
            ) !== -1)
        ) {
          node.expanded = true;
          node.loaded = true;
          resolve(JSON.parse(JSON.stringify(node.data.children)));
          return;
        }
      }
      let $this = this;
      if ($this.config.url !== "") {
        let id = 0;
        let type = "";
        if (node && node.data != undefined && node.data.entryId != undefined) {
          id = node.data.entryId;
          type = node.data.type;
        }
        var queryParams = {};
        for (var i in $this.config.queryParams) {
          if ($this.config.queryParams.hasOwnProperty(i)) {
            queryParams[i] = $this.config.queryParams[i];
          }
        }
        queryParams["id"] = id;
        queryParams["type"] = type;
        queryParams["keyword"] = "";
        if (id === "0") {
          queryParams["isRootNode"] = true;
        } else {
          queryParams["isRootNode"] = false;
        }
        if ($this.config.method.toUpperCase() == "get".toUpperCase()) {
          $this
            .$catch($this.config.url, {
              params: queryParams,
              timeout: 5000
            })
            .then(res => {
              if ($this.config.includeRootNode && id == "0") {
                for (var i = 0; i < this.config.headerNode.length; i++) {
                  res.data.unshift(this.config.headerNode[i]);
                }
              }
              $this.inited = true;
              $this.$emit("treeData", res.data, id);
              return resolve(res.data);
            })
            .catch(err => {
              this.inited = true;
            });
        } else {
          $this
            .$fetch($this.config.url, Qs.stringify(queryParams))
            .then(res => {
              if ($this.config.includeRootNode && id == "0") {
                for (var i = 0; i < this.config.headerNode.length; i++) {
                  res.data.unshift(this.config.headerNode[i]);
                }
              }
              $this.inited = true;
              $this.$emit("treeData", res.data, id);
              resolve(res.data);
            })
            .catch(err => {
              this.inited = true;
            });
        }
      } else {
        if (this.$refs.filterTree !== undefined)
          this.$refs.filterTree.$data.store.lazy = false;
        this.config.data === undefined
          ? resolve([])
          : resolve(this.config.data);
      }
    },
    /**点击事件 */
    clickEvent(data, node) {
      if (
        this.config.selectNodeType === undefined ||
        this.config.selectNodeType === ""
      ) {
        this.$emit("onNodeClick", data, node);
        return;
      }
      if (data.type === this.config.selectNodeType) {
        this.$emit("onNodeClick", data);
      } else {
        this.$refs.filterTree.currentNode.handleExpandIconClick(); //如果设置了selectNodeType 就打开
      }
    },
    /**若节点可被选择（即 show-checkbox 为 true），则返回目前被选中的节点所组成的数组 */
    getCheckedNodes: function() {
      return this.$refs.filterTree.getCheckedNodes();
    },
    /**(leafOnly) 接收一个 boolean 类型的参数，若为 true 则仅返回被选中的叶子节点的 keys，默认值为 false */
    getCheckedKeys: function(leafOnly) {
      return this.$refs.filterTree.getCheckedKeys(leafOnly);
    },
    /**通过 keys 设置目前勾选的节点，使用此方法必须设置 node-key 属性 */
    setCheckedKeys: function(keys, leafOnly) {
      this.$refs.filterTree.setCheckedKeys(keys, leafOnly);
    },
    /**设置目前勾选的节点，使用此方法必须设置 node-key 属性 */
    setCheckedNodes: function(nodes) {
      this.$refs.filterTree.setCheckedNodes(nodes);
    },
    /**
     * (key/data, checked, deep) 接收三个参数，1. 勾选节点的 key 或者 data 2. boolean 类型，节点是否选中 3. boolean 类型，是否设置子节点 ，默认为 false
     */
    setChecked: function(data, checked, deep) {
      this.$refs.filterTree.setChecked(data, checked, deep);
    },
    /**共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点本身是否被选中、节点的子树中是否有被选中的节点 */
    checkChange: function(data, selected, hasChildrenSeletced) {
      this.$emit("checkChange", data, selected, hasChildrenSeletced);
    },
    /* 拖拽事件 */
    mousedownEvent(event) {
      let $this = this;
      this.$emit("mousedownEvent", event);
      if ($this.config.dragTag) {
        let parent = $this.parents(event.target, "el-tree-node__content");
        if (!parent) {
          return false;
        }
        let node = parent.getElementsByClassName("el-tree-node__label")[0];
        if (node.getAttribute("data-type") == $this.config.leafType) {
          let data = node.getElementsByTagName("input")[0].value,
            list = JSON.parse(data);

          let startX = event.clientX,
            startY = event.clientY;
          let div = document.createElement("div");
          div.id = "dragging";
          div.className = "drag-box";
          div.style = "left:" + startX + "px;top:" + startY + "px;";
          div.innerHTML = list.text;
          document.body.appendChild(div);
          document.onmousemove = function(e) {
            //鼠标移动
            if (!e) e = window.event; //移动时创建一个事件
            let dragX = e.clientX + "px",
              dragY = e.clientY + "px";
            let box = document.getElementById("dragging");
            box.style.left = dragX;
            box.style.top = dragY;
          };
          document.onmouseup = function(e) {
            //鼠标放开
            if (!e) e = window.event;
            $this.$emit("dragEnd");
            let endX = e.clientX,
              endY = e.clientY;
            let box = document.getElementById("dragging");
            let index = $this.judgeDrop(endX, endY, $this.config.dropTarget);
            if (index) {
              let node = JSON.parse(data);
              $this.$emit("onDrag", node, index);
            }
            document.body.removeChild(box);
            document.onmousemove = null;
            document.onmouseup = null;
          };
        }
      }
    },
    /* 判断是否在拖拽目标框内 */
    judgeDrop(x, y, target) {
      for (let i = 0; i < target.length; i++) {
        let pos = target[i].getBoundingClientRect();
        let top = pos.top,
          bottom = pos.bottom,
          left = pos.left,
          right = pos.right;
        if (x >= left && x <= right && y >= top && y <= bottom) {
          return i + 1;
        }
      }
      return false;
    },
    /**设置数据,设置后树默认不为lazy */
    setData(data) {
      this.$refs.filterTree.$data.store.lazy = false;
      this.inited = true;
      this.config.data = data;
    },
    /**树节点重写方法 */
    renderContent(h, { node, data, store }) {
      var $this = this;
      if (
        node !== null &&
        node !== undefined &&
        node.childNodes !== undefined &&
        node.childNodes.length > 0
      ) {
        //如果有下级节点返回 直接设置为打开状态
        node.expanded = true;
      }

      if (data.type === this.config.leafType) {
        node.isLeaf = true;
      }
      var pStyle = "";
      if (this.config.includeRootNode) {
        for (var i = 0; i < this.config.headerNode.length; i++) {
          if (data.entryId == this.config.headerNode[i].entryId) {
            pStyle = "width:calc(100% - 6px);margin-left:-18px;";
            node.isLeaf = true;
            break;
          }
        }
      }
      var pText = " ";
      if (typeof node.parent.data === "object") {
        if (node.parent.data.text != undefined) {
          pText = node.parent.data.text;
        } else {
          pText = "根目录";
        }
      }
      if (this.tree.renderFunc != undefined) {
        return this.tree.renderFunc(h, { node, data, store });
      }
      var icon = this.config.icon;
      var classlist = "el-tree-node__label tree-node-id " + data.type;
      return (
        <span
          class={classlist}
          data-id={data.entryId}
          data-pid={data.pid}
          data-type={data.type}
          data-text={data.text}
          data-pText={pText}
          onMouseenter={function($event) {
            if ($this.config.showRightMenu) {
              $event.target.querySelector(".right_menu").style.visibility =
                "visible";
            }
          }}
          onMouseleave={function($event) {
            if ($this.config.showRightMenu) {
              $event.target.querySelector(".right_menu").style.visibility =
                "hidden";
            }
          }}
          style={pStyle}
        >
          {icon[data.type] !== undefined ? (
            <i class={[icon[data.type], "fa", "i-right"]} aria-hidden="true" />
          ) : (
            ""
          )}
          {node.label}
          {$this.config.showRightMenu ? (
            <div
              class="right_menu"
              // onClick={event => {
              //   $this.$emit("onRightMenuClick", event, data);
              // }}
            >
              {data.entryId != "0" &&
              $this.config.iconGroup &&
              $this.config.iconGroup.edit ? (
                <i
                  class="iconfont icon-bda-sys-rename"
                  aria-hidden="true"
                  onClick={event => {
                    $this.$emit("onRightMenuClick", event, data, "edit");
                    event.stopPropagation();
                  }}
                />
              ) : (
                ""
              )}
              {$this.config.iconGroup && $this.config.iconGroup.add ? (
                <i
                  class="iconfont icon-bda-sys-add"
                  aria-hidden="true"
                  onClick={event => {
                    $this.$emit("onRightMenuClick", event, data, "add");
                    event.stopPropagation();
                  }}
                />
              ) : (
                ""
              )}
              {data.entryId != "0" &&
              $this.config.iconGroup &&
              $this.config.iconGroup.delete ? (
                <i
                  class="iconfont icon-bda-sys-remove"
                  aria-hidden="true"
                  onClick={event => {
                    $this.$emit("onRightMenuClick", event, data, "delete");
                    event.stopPropagation();
                  }}
                />
              ) : (
                ""
              )}
            </div>
          ) : (
            ""
          )}
          {$this.config.dragTag && data.type == $this.config.leafType ? (
            <input type="hidden" value={JSON.stringify(data)} />
          ) : (
            ""
          )}
        </span>
      );
    },
    setQueryParams(params) {
      if (params === undefined) return;
      for (var i in params) {
        if (params.hasOwnProperty(i)) {
          this.config.queryParams[i] = params[i];
        }
      }
    },
    /**重新加载树的数据--params为查询参数 */
    reloadEvent(params) {
      let $this = this;
      $this.inited = true;
      $this.keyword = "";
      $this.placeholderFlag = true;
      $this.config.queryParams["keyword"] = "";
      $this.setQueryParams(params);
      let queryParams = {};
      for (var i in $this.config.queryParams) {
        if ($this.config.queryParams.hasOwnProperty(i)) {
          queryParams[i] = $this.config.queryParams[i];
        }
      }
      if (!queryParams["id"] || queryParams["id"] == "") {
        queryParams.id = "0";
      }
      if ($this.config.url === "") return;
      $this.searching = true;
      if ($this.config.method.toUpperCase() == "get".toUpperCase()) {
        $this
          .$catch($this.config.url, {
            params: queryParams,
            timeout: 5000
          })
          .then(res => {
            if (res && res.success == true) {
              $this.$emit("treeData", res.data, queryParams.id);

              $this.$set($this.config, "data", res.data);
              $this.$refs.filterTree.$data.store.defaultExpandedKeys = [];
              $this.$refs.filterTree.$data.store.lazy = true;

              $this.keyFlag = false;
              if ($this.config.includeRootNode) {
                for (var i = 0; i < this.config.headerNode.length; i++) {
                  res.data.unshift(this.config.headerNode[i]);
                }
              }

              $this.searching = false;
            } else {
              $this.messages($this, "error", res.code);
            }
          })
          .catch(err => {
            console.log(err);
          });
      } else {
        $this
          .$fetch($this.config.url, Qs.stringify(queryParams))
          .then(res => {
            if (res && res.success == true) {
              $this.$emit("treeData", res.data, queryParams.id);
              $this.$set($this.config, "data", res.data);
              $this.$refs.filterTree.$data.store.defaultExpandedKeys = [];
              $this.$refs.filterTree.$data.store.lazy = true;

              $this.keyFlag = false;
              if ($this.config.includeRootNode) {
                for (var i = 0; i < this.config.headerNode.length; i++) {
                  res.data.unshift(this.config.headerNode[i]);
                }
              }
              $this.searching = false;
            } else {
              $this.messages($this, "error", res.code);
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    },
    /* 局部更新树的数据 id为更新树的节点 ,nodeIdPre为数据id字段值的前缀，如：id:index_2017,则为index,id:dbtable_2017,则为dbtable*/
    reloadPartEvent(id, nodeIdPre) {
      console.log(id, "进入了局部刷新");
      if (id == "0") {
        this.reloadEvent();
        return;
      }
      let $this = this;
      let queryParams = {};
      for (var i in $this.config.queryParams) {
        if ($this.config.queryParams.hasOwnProperty(i)) {
          queryParams[i] = $this.config.queryParams[i];
        }
      }
      queryParams.id = id;
      queryParams.keyword = "";
      queryParams.type = nodeIdPre.split("_")[0];
      if ($this.config.url === "") return;
      if ($this.config.method.toUpperCase() == "get".toUpperCase()) {
        $this
          .$catch($this.config.url, {
            params: queryParams,
            timeout: 30000
          })
          .then(res => {
            if (res && res.success == true) {
              console.log(res.data, "shuax");
              $this.$refs.filterTree.updateKeyChildren(
                (nodeIdPre || "index_") + id,
                res.data
              );
            } else {
              $this.messages($this, "error", res.code);
            }
          })
          .catch(err => {
            console.log(err);
          });
      } else {
        $this
          .$fetch($this.config.url, Qs.stringify(queryParams))
          .then(res => {
            if (res && res.success == true) {
              console.log(res.data, "shuax");

              $this.$refs.filterTree.updateKeyChildren(
                (nodeIdPre || "index_") + id,
                res.data
              );
            } else {
              $this.messages($this, "error", res.code);
            }
          })
          .catch(err => {
            console.log(err);
          });
      }
    }
  }
};
</script>

<style lang='less'>
</style>

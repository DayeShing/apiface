<!--
  菜单box
  作者：Daye Shing
-->
<template>
  <div class="toolbox">
    <span class="bg_links social portfolio">
      <i
        class="icon"
        :title="info.portfolio"
        @click="clickEvent('portfolio')"
      ></i>
    </span>
    <span class="bg_links social dribbble">
      <i
        class="icon"
        :title="info.dribbble"
        @click="clickEvent('dribbble')"
      ></i>
    </span>
    <span class="bg_links social linkedin">
      <i
        class="icon"
        :title="info.linkedin"
        @click="clickEvent('linkedin')"
      ></i>
    </span>
    <span class="bg_links mores"></span>
  </div>
</template>

<script>
export default {
  props: {
    info: {
      type: Object,
      default() {
        return {
          portfolio: "",
          dribbble: "",
          linkedin: "",
        };
      },
    },
  },
  methods: {
    clickEvent(value) {
      this.$emit("selectMenu", value);
    },
  },
};
</script>

<style lang="less">
.toolbox {
  position: fixed;
  z-index: 10;
  bottom: 10px;
  right: 10px;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  transition: all 0.2s ease;
  .bg_links {
    width: 40px;
    height: 40px;
    border-radius: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 100%;
    backdrop-filter: blur(5px);
    position: absolute;
  }
  .mores {
    width: 40px;
    height: 40px;
    z-index: 9;
    background-image: url(./svg/logo_white.svg);
    background-size: 50%;
    background-repeat: no-repeat;
    background-position: 10px 7px;
    opacity: 0.9;
    transition: all 1s 0.2s ease;
    bottom: 0;
    right: 0;
  }
  .social {
    opacity: 0;
    right: 0;
    bottom: 0;
  }
  .social .icon {
    width: 100%;
    height: 100%;
    background-size: 20px;
    background-repeat: no-repeat;
    background-position: center;
    background-color: transparent;
    display: flex;
    transition: all 0.2s ease, background-color 0.4s ease;
    opacity: 0;
    border-radius: 100%;
  }
  .social.portfolio {
    transition: all 0.8s ease;
    .icon {
      background-image: url(./svg/link.svg);
    }
  }
  .social.dribbble {
    transition: all 0.3s ease;
    .icon {
      background-image: url(./svg/dribbble.svg);
    }
  }
  .social.linkedin {
    transition: all 0.8s ease;
    .icon {
      background-image: url(./svg/linkedin.svg);
    }
  }

  &:hover {
    width: 105px;
    height: 105px;
    transition: all 0.6s cubic-bezier(0.64, 0.01, 0.07, 1.65);
    .mores {
      opacity: 1;
      transition: all 0.6s ease;
    }
    .social {
      opacity: 1;
      .icon {
        opacity: 0.9;
      }
      &:hover {
        background-size: 28px;
        .icon {
          background-size: 65%;
          opacity: 1;
        }
      }
    }

    .social.portfolio {
      right: 0;
      bottom: calc(100% - 40px);
      transition: all 0.3s 0s cubic-bezier(0.64, 0.01, 0.07, 1.65);
      .icon:hover {
        background-color: #698fb7;
      }
    }
    .social.dribbble {
      bottom: 45%;
      right: 45%;
      transition: all 0.3s 0.15s cubic-bezier(0.64, 0.01, 0.07, 1.65);
      .icon:hover {
        background-color: #ea4c89;
      }
    }
    .social.linkedin {
      bottom: 0;
      right: calc(100% - 40px);
      transition: all 0.3s 0.25s cubic-bezier(0.64, 0.01, 0.07, 1.65);
      .icon:hover {
        background-color: #0077b5;
      }
    }
  }
}
</style>

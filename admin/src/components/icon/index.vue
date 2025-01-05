<script lang="ts">
import { defineComponent, createVNode } from 'vue'
import { ElIcon } from 'element-plus'
import svgIcon from './svg-icon.vue'
import { EL_ICON_PREFIX, LOCAL_ICON_PREFIX } from '@/components/icon/index'

export default defineComponent({
  name: 'Icon', // 组件名称
  props: {
    // 图标自定义属性
    name: {
      type: String,
      required: true,
    },
    size: {
      type: [String, Number],
      default: '14px',
    },
    color: {
      type: String,
      default: 'inherit',
    },
  },
  setup(props) {
    // element-plus图标
    if (props.name.indexOf(EL_ICON_PREFIX) === 0) {
      return () =>
        createVNode(
          ElIcon,
          {
            size: props.size,
            color: props.color,
          },
          () => [
            createVNode(
              resolveComponent(props.name.replace(EL_ICON_PREFIX, '')),
            ),
          ],
        )
    }
    // 本地图标
    if (props.name.indexOf(LOCAL_ICON_PREFIX) === 0) {
      return () =>
        h(
          'i',
          {
            class: ['local-icon'],
          },
          createVNode(svgIcon, { ...props }),
        )
    }
  },
})
</script>

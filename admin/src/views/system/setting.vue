<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    @select="handleSelect"
  >
    <el-menu-item index="web">网站设置</el-menu-item>

    <el-menu-item index="logo">网站LOGO设置</el-menu-item>
    <el-menu-item index="qrcode">二维码配置</el-menu-item>
    <el-menu-item index="official">轮播图配置</el-menu-item>
    <el-menu-item index="excel">Excel导入模版配置</el-menu-item>
    <el-menu-item index="ximg">X光默认描述图片配置</el-menu-item>
  </el-menu>
  <!--  网站LOGO配置-->
  <el-card class="!border-none" shadow="never" v-if="activeIndex === 'logo'">
    <div class="flex flex-wrap gap-32">
      <div class="flex flex-col items-center gap-4">
        <upload
          :src="configFormData.webFavicon.value as string"
          file-path="config"
          :show-file-list="false"
          :width="200"
          :height="200"
          @success="(files: string) => handleSuccess(files, 'webFavicon')"
        />
        <div>
          {{ configFormData.webFavicon.label }}
        </div>
      </div>
      <div class="flex flex-col items-center gap-4">
        <upload
          :src="configFormData.webLogo.value as string"
          file-path="config"
          :show-file-list="false"
          :width="200"
          :height="200"
          @success="(files: string) => handleSuccess(files, 'webLogo')"
        />
        <div class="mb-2 ml-1">
          {{ configFormData.webLogo.label }}
        </div>
      </div>
      <div class="flex flex-col items-center gap-4">
        <upload
          :src="configFormData.loginBg.value as string"
          file-path="config"
          :show-file-list="false"
          :width="200"
          :height="200"
          @success="(files: string) => handleSuccess(files, 'loginBg')"
        />
        <div class="mb-2 ml-1">
          {{ configFormData.loginBg.label }}
        </div>
      </div>
      <div class="flex flex-col items-center gap-4">
        <upload
          :src="configFormData.loginFooterBg.value as string"
          file-path="config"
          :show-file-list="false"
          :width="200"
          :height="200"
          @success="(files: string) => handleSuccess(files, 'loginFooterBg')"
        />
        <div class="mb-2 ml-1">
          {{ configFormData.loginFooterBg.label }}
        </div>
      </div>
    </div>
  </el-card>
  <!--  网站设置-->
  <el-card
    class="!border-none"
    shadow="never"
    v-else-if="activeIndex === 'web'"
  >
    <el-row :gutter="10" class="mb-4">
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col">
          <div class="mb-4 ml-1">
            {{ configFormData.webName.label }}
            <span class="text-info text-xs">
              （{{ configFormData.webName.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.webName.value" size="large" />
        </div>
      </el-col>
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.filings.label }}和{{
              configFormData.filingsName.label
            }}
          </div>
          <div class="flex items-center gap-2">
            <el-input
              class="flex-1"
              v-model="configFormData.filings.value"
              size="large"
              :placeholder="configFormData.filings.remark"
            />
            <el-input
              class="flex-1"
              v-model="configFormData.filingsName.value"
              size="large"
              :placeholder="configFormData.filingsName.remark"
            />
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="40" class="mb-4">
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.theme.label }}
            <span class="text-info text-xs">
              （ {{ configFormData.theme.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.theme.value" size="large" />
        </div>
      </el-col>
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col">
          <div class="mb-4 ml-1">
            {{ configFormData.mobileTheme.label }}
            <span class="text-info text-xs">
              （{{ configFormData.mobileTheme.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.mobileTheme.value" size="large" />
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="40" class="mb-4">
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.serverUrl.label }}
            <span class="text-info text-xs">
              （ {{ configFormData.serverUrl.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.serverUrl.value" size="large" />
        </div>
      </el-col>
      <el-col :span="12" :xs="24" :sm="24" :md="12">
        <div class="flex flex-col">
          <div class="mb-4 ml-1">
            {{ configFormData.filePath.label }}
            <span class="text-info text-xs">
              （{{ configFormData.filePath.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.filePath.value" size="large" />
        </div>
      </el-col>
    </el-row>
  </el-card>
  <!--  二维码配置-->
  <el-card
    class="!border-none"
    shadow="never"
    v-else-if="activeIndex === 'qrcode'"
  >
    <el-row :gutter="20">
      <el-col :span="6" :xs="24" :sm="24" :md="12" :lg="6" class="mb-4">
        <div class="flex items-center gap-4">
          {{ configFormData.qrcodeWidth.label }}
          <el-input-number
            v-model="configFormData.qrcodeWidth.value as number"
          />
        </div>
      </el-col>
      <el-col :span="6" :xs="24" :sm="24" :md="12" :lg="6" class="mb-4">
        <div class="flex items-center gap-4">
          {{ configFormData.qrcodeHeight.label }}
          <el-input-number
            v-model="configFormData.qrcodeHeight.value as number"
          />
        </div>
      </el-col>
      <el-col :span="6" :xs="24" :sm="24" :md="12" :lg="6" class="mb-4">
        <div class="flex items-center gap-4">
          {{ configFormData.qrcodeFontSize.label }}
          <el-input-number
            v-model="configFormData.qrcodeFontSize.value as number"
          />
        </div>
      </el-col>
      <el-col :span="6" :xs="24" :sm="24" :md="12" :lg="6" class="mb-4">
        <div class="flex items-center gap-4">
          {{ configFormData.qrcodeMargin.label }}
          <el-input-number
            v-model="configFormData.qrcodeMargin.value as number"
          />
        </div>
      </el-col>
    </el-row>
  </el-card>
  <!--excel导入模版配置-->
  <el-card
    class="!border-none"
    shadow="never"
    v-else-if="activeIndex === 'excel'"
  >
    <el-row :gutter="10" class="mb-4">
      <el-col :span="12" :xs="24" :sm="24" :md="24" :lg="8">
        <div class="flex flex-col">
          <div class="mb-4 ml-1">
            {{ configFormData.headerBgColor.label }}
            <span class="text-info text-xs">
              （{{ configFormData.headerBgColor.remark }}）
            </span>
          </div>
          <el-input v-model="configFormData.headerBgColor.value" size="large" />
        </div>
      </el-col>
      <el-col :span="12" :xs="24" :sm="24" :md="24" :lg="8">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.headerTextColor.label }}
            <span class="text-info text-xs">
              （{{ configFormData.headerTextColor.remark }}）
            </span>
          </div>
          <el-input
            class="flex-1"
            v-model="configFormData.headerTextColor.value"
            size="large"
            :placeholder="configFormData.headerTextColor.remark"
          />
        </div>
      </el-col>
      <el-col :span="12" :xs="24" :sm="24" :md="24" :lg="8">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.headerTextColor.label }}
            <span class="text-info text-xs">
              （{{ configFormData.headerTextColor.remark }}）
            </span>
          </div>
          <el-input
            class="flex-1"
            v-model="configFormData.headerTextColor.value"
            size="large"
            :placeholder="configFormData.headerTextColor.remark"
          />
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="40" class="mb-4">
      <el-col :span="24">
        <div class="flex flex-col mx-auto">
          <div class="mb-4 ml-1">
            {{ configFormData.templateDescription.label }}
            <span class="text-info text-xs">
              （ {{ configFormData.templateDescription.remark }}）
            </span>
          </div>
          <el-input
            type="textarea"
            :autosize="{ minRows: 2 }"
            v-model="configFormData.templateDescription.value"
            size="large"
          />
        </div>
      </el-col>
    </el-row>
  </el-card>
  <!--x光默认描述图片配置-->
  <el-card
    class="!border-none"
    shadow="never"
    v-else-if="activeIndex === 'ximg'"
  >
    <div class="flex flex-wrap gap-32">
      <div
        class="flex flex-col items-center gap-4"
        v-for="item in proNameDefaultXImg.imgList"
        :key="item.name"
      >
        <upload
          :src="item.src as string"
          file-path="ximg"
          :show-file-list="false"
          :width="200"
          :height="200"
          @success="(files: string) => handleSuccess(files, 'ximg', item.value)"
        />
        <div>{{ item.name }}默认X光图片</div>
      </div>
    </div>
  </el-card>
  <!--轮播图配置-->
  <el-card class="!border-none" shadow="never" v-else>
    <el-row :gutter="40">
      <el-col class="mb-4" :span="8" :xs="24" :sm="24" :md="24" :lg="8">
        <el-card shadow="never" header="效果预览">
          <div class="flex items-center justify-center">
            <div
              class="w-[375px] bg-page h-[667px] border-solid border-[1px] border-info box-content"
            >
              <van-swipe
                indicator-color="white"
                :autoplay="configFormData.autoplay.value"
              >
                <van-swipe-item
                  :style="{ width: '375px', height: `${375 / (4 / 3)}px` }"
                  v-for="(item, index) in configFormData.bannerImgs.imgList"
                  :key="index"
                >
                  <van-image
                    width="375"
                    :height="375 / (4 / 3)"
                    position="center"
                    :src="item.src"
                  />
                </van-swipe-item>
              </van-swipe>
              <div class="flex items-center justify-center py-5 flex-col">
                <van-icon
                  name="scan"
                  size="100"
                  :color="appStore.config.mobileTheme"
                />
                <div class="text-mobilePrimary font-bold text-center mt-1">
                  点击扫码
                </div>
              </div>
              <div class="flex items-center justify-center px-4 gap-2">
                <van-field
                  input-align="center"
                  readonly
                  placeholder="二维码编码/产品编码"
                />
              </div>
              <div class="flex items-center justify-center px-4 mt-5">
                <van-button type="primary" block>
                  <span class="text-nowrap px-4">查询</span>
                </van-button>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col class="mb-4" :span="16" :xs="24" :sm="24" :md="24" :lg="16">
        <el-card shadow="never">
          <template #header>
            <div class="flex items-center justify-between">
              <span>轮播图编辑</span>
              <el-button type="primary" @click="handleAdd">
                <template #icon>
                  <icon name="el-icon-Plus" />
                </template>
                添加
              </el-button>
            </div>
          </template>

          <el-row class="mb-4">
            <el-col :span="24">
              <div class="flex gap-4 items-center">
                <span>播放速度（毫秒）</span>
                <el-input-number
                  v-model="configFormData.autoplay.value as number"
                  :min="0"
                  size="large"
                />
                <span class="text-info text-xs">
                  1秒=1000毫秒，设置为0时不播放
                </span>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col
              class="mb-4"
              :span="12"
              :xs="24"
              :sm="24"
              :md="12"
              v-for="(item, index) in configFormData.bannerImgs.imgList"
              :key="index"
            >
              <el-card shadow="never">
                <template #header>
                  <div
                    class="flex items-center justify-between cursor-pointer"
                    @click="handleDelete(index)"
                  >
                    <span>轮播图片{{ index + 1 }}</span>
                    <icon size="20" name="el-icon-Close" />
                  </div>
                </template>
                <el-row>
                  <el-col :span="24">
                    <div class="flex gap-4 items-center">
                      <span>播放顺序</span>
                      <el-input-number
                        v-model="item.sort as number"
                        :min="1"
                        size="large"
                      />
                      <span class="text-info text-xs">数值越小越靠前</span>
                    </div>
                  </el-col>
                  <el-col :span="24" class="mt-4">
                    <div class="flex gap-4">
                      <span>上传图片</span>
                      <div>
                        <upload
                          :src="item.src as string"
                          file-path="config"
                          :width="150"
                          :show-file-list="false"
                          :aspect-ratio="4 / 3"
                          @success="
                            (files: string) =>
                              handleSuccess(files, 'bannerImgs', index)
                          "
                        />
                        <span class="text-info text-xs">
                          建议图片尺寸按照宽度750像素进行设计，宽高比例为4:3
                        </span>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </el-card>

  <footer-btns>
    <el-button type="primary" :loading="loading" @click="handleSubmit">
      保存
    </el-button>
  </footer-btns>
</template>
<script setup lang="ts">
import {
  getConfigList,
  saveConfigList,
  SaveConfigRequest,
  type SystemConfigMo,
} from '@/api/config'
import Upload from '@/components/upload/index.vue'
import feedback from '@/utils/feedback'
import useSettingStore from '@/stores/modules/setting.ts'
import { useDark } from '@vueuse/core'
import useAppStore from '@/stores/modules/app.ts'
import { useDictData } from '@/hooks/useDictOptions.ts'

const activeIndex = ref<
  'web' | 'logo' | 'qrcode' | 'official' | 'excel' | 'ximg'
>('web')
const loading = ref(false)
const isDark = useDark()
const appStore = useAppStore()
const settingsStore = useSettingStore()

interface BannerImgs extends Partial<SystemConfigMo> {
  imgList: Array<{ src: string; sort: number }>
}

interface ConfigFormData {
  filings: Partial<SystemConfigMo>
  filingsName: Partial<SystemConfigMo>
  webName: Partial<SystemConfigMo>
  theme: Partial<SystemConfigMo>
  mobileTheme: Partial<SystemConfigMo>
  webFavicon: Partial<SystemConfigMo>
  webLogo: Partial<SystemConfigMo>
  loginBg: Partial<SystemConfigMo>
  loginFooterBg: Partial<SystemConfigMo>
  // 二维码配置
  qrcodeFontSize: Partial<SystemConfigMo>
  qrcodeMargin: Partial<SystemConfigMo>
  qrcodeWidth: Partial<SystemConfigMo>
  qrcodeHeight: Partial<SystemConfigMo>
  // 轮播图配置
  bannerImgs: BannerImgs
  autoplay: Partial<SystemConfigMo>
  // 文件上传服务器和文件路径
  serverUrl: Partial<SystemConfigMo>
  filePath: Partial<SystemConfigMo>
  // 导入模版配置
  headerBgColor: Partial<SystemConfigMo>
  headerTextColor: Partial<SystemConfigMo>
  templateDescription: Partial<SystemConfigMo>
  headerRowHeight: Partial<SystemConfigMo>
}

const configFormData = reactive<ConfigFormData>({
  filings: {}, // 网站备案链接
  filingsName: {}, // 网站备案公安部名称
  webName: {}, // 网站名称
  theme: {}, // pc端主题颜色
  mobileTheme: {}, // 公众号查询主题颜色
  webFavicon: {}, // 网站logo
  webLogo: {}, // 登录界面logo
  loginBg: {}, // 登录界面背景图
  loginFooterBg: {}, // 登录界面底部背景图
  // 二维码配置
  qrcodeFontSize: {},
  qrcodeMargin: {},
  qrcodeWidth: {},
  qrcodeHeight: {},
  // 轮播图配置
  bannerImgs: {
    imgList: [],
  },
  autoplay: {},
  // 文件上传配置
  serverUrl: {},
  filePath: {},
  // 导入模版配置
  headerBgColor: {},
  headerTextColor: {},
  templateDescription: {},
  headerRowHeight: {},
})
// 产品X光默认图片显示
const proNameDefaultXImg = reactive<{
  id?: number
  name?: string
  imgList?: {
    dictType: string
    src: string
    name: string
    value: string
  }[]
}>({})
const { dictData } = useDictData<{
  proName: any[]
}>(['proName'])
const handleSelect = (index: 'web' | 'logo' | 'qrcode' | 'official') => {
  activeIndex.value = index
}
// 获取系统配置列表
const loadConfigList = async () => {
  try {
    const data = await getConfigList()
    data.forEach((item) => {
      let images: Array<{
        src: string
        dictType: string
        name: string
        value: string
      }> = []
      switch (item.name) {
        case 'bannerImgs':
          try {
            if (item.value) {
              const images = JSON.parse(item.value as string) as Array<{
                src: string
                sort: number
              }>
              for (let key in item) {
                configFormData.bannerImgs[key] = item[key]
              }
              configFormData.bannerImgs.imgList = images.map((img) => ({
                src: img.src,
                sort: img.sort,
              }))
              configFormData.bannerImgs.imgList.sort()
            }
          } catch (err) {
            // TODO:
          }
          break
        case 'proNameDefaultXImg':
          proNameDefaultXImg.id = item.id
          proNameDefaultXImg.name = item.name
          if (item.value) {
            images = JSON.parse(item.value as string) as Array<{
              src: string
              dictType: string
              name: string
              value: string
            }>
          }
          proNameDefaultXImg.imgList = dictData.proName.map((item) => {
            const img = images.find((img) => img.value === item.value)
            return {
              src: img ? img.src : '',
              dictType: item.dictType,
              name: item.name,
              value: item.value,
            }
          })
          break
        default:
          configFormData[item.name] = item
          break
      }
    })
    console.log(proNameDefaultXImg)
  } catch (error) {
    console.error('获取系统配置列表出错：', error)
  }
}

// 新增轮播项
const handleAdd = () => {
  const bannerItem = {
    src: '',
    sort: 1,
  }
  configFormData.bannerImgs.imgList.push(bannerItem)
  configFormData.bannerImgs.imgList.sort()
  configFormData.bannerImgs.value = JSON.stringify(
    configFormData.bannerImgs.imgList,
  )
}

// 删除轮播项
const handleDelete = (index: number) => {
  configFormData.bannerImgs.imgList.splice(index, 1)
  configFormData.bannerImgs.imgList.sort()
  configFormData.bannerImgs.value = JSON.stringify(
    configFormData.bannerImgs.imgList,
  )
}

// 图片上传成功
const handleSuccess = (url: string, prop: string, index?: number | string) => {
  if (prop === 'bannerImgs') {
    configFormData.bannerImgs.imgList[index as number].src = url
    configFormData.bannerImgs.value = JSON.stringify(
      configFormData.bannerImgs.imgList,
    )
  } else if (prop === 'ximg') {
    proNameDefaultXImg.imgList?.some((item) => {
      if (item.value === index) {
        item.src = url
        return true
      }
    })
  } else {
    configFormData[prop].value = url
  }
}

const handleSubmit = async () => {
  try {
    loading.value = true
    const req: SaveConfigRequest[] = []
    for (const key in configFormData) {
      const item = configFormData[key]
      req.push({
        id: item.id as number,
        value: item.value,
      })
    }
    if (proNameDefaultXImg.imgList && proNameDefaultXImg.imgList.length) {
      req.push({
        id: proNameDefaultXImg.id as number,
        value: JSON.stringify(proNameDefaultXImg.imgList),
      })
    }
    await saveConfigList(req)
    await appStore.getConfig()
    settingsStore.setTheme(isDark.value)
    feedback.msgSuccess('保存成功')
  } catch (err) {
    console.error('保存系统配置列表出错：', err)
  } finally {
    loading.value = false
  }
}

loadConfigList()
</script>

<template>
  <div class="login flex flex-col">
    <div class="flex-1 flex items-center justify-center">
      <div class="login-card flex rounded-md">
        <div class="flex-1 h-full hidden md:inline-block">
          <image-contain :src="config['webLogo']" :width="400" height="100%" />
        </div>
        <div
          class="login-form bg-body flex flex-col justify-center px-10 py-10 md:w-[400px] w-[375px] flex-none mx-auto"
        >
          <div class="text-center text-3xl font-medium mb-8">
            {{ config['webName'] || '苗医药管理系统' }}
          </div>
          <el-form ref="formRef" :model="formData" size="large" :rules="rules">
            <el-form-item prop="account">
              <el-input
                v-model.trim="formData.account"
                placeholder="请输入账号"
                @keyup.enter="handleEnter"
              >
                <template #prepend>
                  <icon name="el-icon-User" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                ref="passwordRef"
                v-model="formData.password"
                show-password
                placeholder="请输入密码"
                @keyup.enter="handleLogin"
              >
                <template #prepend>
                  <icon name="el-icon-Lock" />
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="code">
              <div class="flex items-center">
                <el-input
                  v-model="formData.code"
                  placeholder="请输入验证码"
                  @keyup.enter="handleLogin"
                >
                  <template #prepend>
                    <icon name="local-icon-anquan" />
                  </template>
                </el-input>
                <div
                  class="ml-4 w-[100px] flex-none cursor-pointer"
                  @click="getLoginCaptcha"
                >
                  <img class="w-full" :src="codeImg" alt="点击刷新" />
                </div>
              </div>
            </el-form-item>
          </el-form>
          <div class="mb-5">
            <el-checkbox v-model="remAccount" label="记住账号"></el-checkbox>
          </div>
          <el-button
            type="primary"
            size="large"
            :loading="isLock"
            @click="lockLogin"
          >
            登录
          </el-button>
        </div>
      </div>
    </div>
    <layout-footer />
  </div>
</template>

<script lang="ts" setup>
import { captcha, getPublicKey } from '@/api/user'
import { FormInstance, InputInstance } from 'element-plus'
import cache from '@/utils/cache'
import { ACCOUNT_KEY } from '@/enums/cacheEnum'
import { JSEncrypt } from 'jsencrypt'
import useUserStore from '@/stores/modules/user'
import { PageEnum } from '@/enums/pageEnum.ts'
import { useLockFn } from '@/hooks/useLockFn'
import useAppStore from '@/stores/modules/app'
import LayoutFooter from '@/layout/components/footer.vue'

const userStore = useUserStore()
const { config } = useAppStore()
const route = useRoute()
const router = useRouter()
const passwordRef = shallowRef<InputInstance>()
const formRef = shallowRef<FormInstance>()
const remAccount = ref(true) // 记住账号
const codeImg = ref('') // 图片
const publicKey = ref('') // 密码加密公钥
const formData: Record<string, any> = reactive({
  code: '',
  password: '',
  account: '',
  uuid: '',
})

// 登录
const handleLogin = async () => {
  try {
    // 验证登录表单的数据字段
    await formRef.value?.validate()
    const data = {
      code: formData.code,
      password: formData.password,
      account: formData.account,
      captchaKey: formData.uuid,
    }
    // 将密码进行加密
    const jsEncrypt = new JSEncrypt()
    jsEncrypt.setPublicKey(publicKey.value)
    const encrypt = jsEncrypt.encrypt(JSON.stringify(data))
    if (typeof encrypt === 'string') {
      await userStore.login(encrypt)
      // 如果已经记住账号,将账号保存到缓存中
      if (remAccount.value) {
        cache.setItem(ACCOUNT_KEY, {
          remember: remAccount.value,
          account: formData.account,
        })
      }
      // 登录成功,跳转到重定向的界面或者首页
      const path =
        typeof route.query.redirect === 'string'
          ? route.query.redirect
          : PageEnum.INDEX
      router.push(path)
    }
    throw new Error('加密失败')
  } catch {
    queryPublicKey()
    getLoginCaptcha()
  }
}

const { isLock, lockFn: lockLogin } = useLockFn(handleLogin)

const rules = {
  account: [
    {
      required: true,
      message: '请输入账号',
      trigger: ['blur'],
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: ['blur'],
    },
  ],
  code: [
    {
      required: true,
      message: '请输入验证码',
      trigger: ['blur'],
    },
  ],
}

// 获取图片验证码
const getLoginCaptcha = async () => {
  const data = await captcha()
  formData.uuid = data.key
  codeImg.value = data.url
}

// 获取加密公钥
const queryPublicKey = async () => {
  const data = await getPublicKey()
  publicKey.value = data.publicKey
}

// 回车按键监听
const handleEnter = () => {
  if (!formData.password) {
    return passwordRef.value?.focus()
  }
  handleLogin()
}

onMounted(() => {
  let account = JSON.parse(cache.getItem(ACCOUNT_KEY)) as {
    remember: boolean
    account: string
  }
  queryPublicKey()
  getLoginCaptcha()
  if (account?.remember) {
    remAccount.value = account.remember
    formData.account = account.account
  }
})
</script>

<style lang="scss" scoped>
.login {
  background-image: url('./images/login_bg.png');
  @apply min-h-screen bg-no-repeat bg-center bg-cover;
  .login-card {
    height: 400px;
    :deep(.el-input-group__prepend) {
      padding: 0 15px;
    }
  }
}
</style>

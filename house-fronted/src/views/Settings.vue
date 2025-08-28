<template>
  <div class="settings-container">
    <div class="settings-header">
      <h1 class="settings-title">
        <i class="icon-settings">⚙️</i>
        系统设置
      </h1>
      <p class="settings-subtitle">个性化您的使用体验</p>
    </div>

    <div class="settings-content">
      <!-- 字体大小设置 -->
      <div class="setting-section">
        <div class="section-header">
          <h2 class="section-title">
            <i class="icon-font">🔤</i>
            字体大小
          </h2>
          <p class="section-description">调整系统界面的字体大小</p>
        </div>
        
        <div class="font-size-options">
          <div 
            v-for="(option, key) in FONT_SIZES" 
            :key="key"
            class="font-option"
            :class="{ active: fontSize === key }"
            @click="setFontSize(key as FontSizeKey)"
          >
            <div class="option-preview" :style="{ fontSize: option.value }">
              Aa
            </div>
            <div class="option-info">
              <span class="option-label">{{ option.label }}</span>
              <span class="option-value">{{ option.value }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 背景主题设置 -->
      <div class="setting-section">
        <div class="section-header">
          <h2 class="section-title">
            <i class="icon-theme">🎨</i>
            背景主题
          </h2>
          <p class="section-description">选择您喜欢的背景颜色主题</p>
        </div>
        
        <div class="theme-options">
          <div 
            v-for="(theme, key) in BACKGROUND_THEMES" 
            :key="key"
            class="theme-option"
            :class="{ active: backgroundTheme === key }"
            @click="setBackgroundTheme(key as BackgroundThemeKey)"
          >
            <div 
              class="theme-preview"
              :style="{ background: theme.value }"
            >
              <div class="theme-check" v-if="backgroundTheme === key">
                ✓
              </div>
            </div>
            <span class="theme-label">{{ theme.label }}</span>
          </div>
        </div>
      </div>

      <!-- 预览区域 -->
      <div class="setting-section">
        <div class="section-header">
          <h2 class="section-title">
            <i class="icon-preview">👁️</i>
            效果预览
          </h2>
          <p class="section-description">查看当前设置的效果</p>
        </div>
        
        <div class="preview-area" :style="{ background: currentBackgroundTheme.value }">
          <div class="preview-content" :style="{ fontSize: currentFontSize.value }">
            <h3>房客管理系统</h3>
            <p>这是一个示例文本，用于预览当前的字体大小和背景主题效果。</p>
            <div class="preview-buttons">
              <button class="preview-btn primary">主要按钮</button>
              <button class="preview-btn secondary">次要按钮</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="settings-actions">
        <button class="action-btn reset" @click="resetSettings">
          <i class="icon-reset">🔄</i>
          重置默认
        </button>
        <button class="action-btn save" @click="saveAndApply">
          <i class="icon-save">💾</i>
          保存设置
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from 'pinia'
import { useSettingsStore, FONT_SIZES, BACKGROUND_THEMES } from '../stores/settings'
import type { FontSizeKey, BackgroundThemeKey } from '../stores/settings'

const settingsStore = useSettingsStore()
const { 
  fontSize, 
  backgroundTheme, 
  currentFontSize, 
  currentBackgroundTheme 
} = storeToRefs(settingsStore)

const { 
  setFontSize, 
  setBackgroundTheme, 
  resetSettings, 
  saveSettings, 
  applySettings 
} = settingsStore

// 保存并应用设置
const saveAndApply = () => {
  saveSettings()
  applySettings()
  // 显示保存成功提示
  showSuccessMessage()
}

// 显示成功消息
const showSuccessMessage = () => {
  // 这里可以集成消息提示组件
  alert('设置已保存！')
}
</script>

<style scoped>
.settings-container {
  min-height: 100vh;
  padding: 2rem;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
}

.settings-header {
  text-align: center;
  margin-bottom: 3rem;
}

.settings-title {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.settings-subtitle {
  color: #6b7280;
  font-size: 1.1rem;
}

.settings-content {
  max-width: 800px;
  margin: 0 auto;
}

.setting-section {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.section-header {
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #374151;
  margin-bottom: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.section-description {
  color: #6b7280;
  font-size: 0.95rem;
}

/* 字体大小选项 */
.font-size-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
}

.font-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 1.5rem;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fafafa;
}

.font-option:hover {
  border-color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.15);
}

.font-option.active {
  border-color: #667eea;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.option-preview {
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #374151;
}

.font-option.active .option-preview {
  color: white;
}

.option-info {
  text-align: center;
}

.option-label {
  display: block;
  font-weight: 500;
  margin-bottom: 0.25rem;
}

.option-value {
  font-size: 0.85rem;
  opacity: 0.8;
}

/* 主题选项 */
.theme-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 1rem;
}

.theme-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.theme-option:hover {
  transform: translateY(-3px);
}

.theme-preview {
  width: 80px;
  height: 60px;
  border-radius: 12px;
  margin-bottom: 0.5rem;
  position: relative;
  border: 3px solid transparent;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-option.active .theme-preview {
  border-color: #374151;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
}

.theme-check {
  color: white;
  font-size: 1.2rem;
  font-weight: bold;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.theme-label {
  font-size: 0.9rem;
  font-weight: 500;
  color: #374151;
  text-align: center;
}

/* 预览区域 */
.preview-area {
  border-radius: 16px;
  padding: 2rem;
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.preview-content {
  position: relative;
  z-index: 1;
}

.preview-content h3 {
  margin-bottom: 1rem;
  font-weight: 600;
}

.preview-content p {
  margin-bottom: 1.5rem;
  opacity: 0.9;
  line-height: 1.6;
}

.preview-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.preview-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.preview-btn.primary {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.preview-btn.secondary {
  background: transparent;
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.preview-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

/* 操作按钮 */
.settings-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 2rem;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 1rem;
}

.action-btn.reset {
  background: #f3f4f6;
  color: #374151;
  border: 1px solid #d1d5db;
}

.action-btn.reset:hover {
  background: #e5e7eb;
  transform: translateY(-2px);
}

.action-btn.save {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.action-btn.save:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .settings-container {
    padding: 1rem;
  }
  
  .setting-section {
    padding: 1.5rem;
  }
  
  .font-size-options,
  .theme-options {
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
  }
  
  .settings-actions {
    flex-direction: column;
  }
  
  .action-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
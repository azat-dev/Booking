import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import KEEP_NAMES from "./vite-keep-names";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react()
    ],
    build: {
        minify: 'terser',
        terserOptions: {
            mangle: {
                keep_classnames: new RegExp(KEEP_NAMES.map(item => `^(${item}$)`).join('|')),
            }
        },
    }
})

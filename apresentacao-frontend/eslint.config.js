import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import tseslint from 'typescript-eslint'
import { defineConfig, globalIgnores } from 'eslint/config'
import stylistic from '@stylistic/eslint-plugin';
import tsParser from '@typescript-eslint/parser';
import tsPlugin from '@typescript-eslint/eslint-plugin';
import tsdoc from 'eslint-plugin-tsdoc';

const plugins = {
  '@stylistic': stylistic,
  '@typescript-eslint': tsPlugin,
  tsdoc,
};

const ignored = Object.fromEntries([
].map(rule => [rule, 'off']));

const rules = {
  ...tsPlugin.configs.recommended.rules,
  ...ignored,
  // Core ESLint rules
  'camelcase': ['warn'],
   'no-restricted-imports': ['error', {
    patterns: [{
      group: ['.*', '..*'],
      message: 'Please use absolute imports with "@"'
    }]
  }],
  'no-self-compare': 'error',
  'no-unmodified-loop-condition': 'warn',
  'no-unreachable-loop': 'error',
  'prefer-const': 'error',
  'quotes': ['error', 'single', { avoidEscape: true }],
  'key-spacing': ['error', {
    beforeColon: false,
    afterColon: true
  }],

  // Stylistic / formatting
  '@stylistic/comma-dangle': ['error', 'never'],
  '@stylistic/eol-last': ['error', 'always'],
  '@stylistic/indent': ['error', 2],
  '@stylistic/no-multi-spaces': 'error',
  '@stylistic/no-multiple-empty-lines': ['error', { max: 1, maxEOF: 0 }],
  '@stylistic/no-trailing-spaces': 'error',
  '@stylistic/object-curly-spacing': ['error', 'always'],
  '@stylistic/semi': 'error',
  '@stylistic/space-before-blocks': ['error', 'always'],
  '@stylistic/space-infix-ops': 'error',
  '@stylistic/type-annotation-spacing': ['error', {
    before: false,
    after: true,
    overrides: { arrow: { before: true, after: true } }
  }],

  // TypeScript rules
  '@typescript-eslint/no-unused-vars': ['warn', {
    args: 'all',
    argsIgnorePattern: '^_',
    caughtErrors: 'all',
    caughtErrorsIgnorePattern: '^_',
    destructuredArrayIgnorePattern: '^_',
    ignoreRestSiblings: true,
    varsIgnorePattern: '^_'
  }],

  // TSDoc rules
  'tsdoc/syntax': 'warn',
}

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      js.configs.recommended,
      tseslint.configs.recommended,
      reactHooks.configs['recommended-latest'],
      reactRefresh.configs.vite,
    ],
    languageOptions: {
      ecmaVersion: 'latest',
      globals: globals.browser,
      parser: tsParser,
    },
    rules,
    plugins
  },
])

module.exports = {
	root: true,
	extends: [
		'@react-native',
		'plugin:import/recommended',
		'plugin:import/errors',
		'plugin:import/warnings',
		'plugin:import/typescript'
	],
	parser: '@typescript-eslint/parser',
	plugins: ['react', 'react-native', 'import', 'prefer-arrow', '@typescript-eslint'],
	settings: {
		'import/ignore': ['react-native', 'src/constants/theme.js'],
		'import/parsers': {
			'@typescript-eslint/parser': ['.js', '.ts', '.tsx']
		},
		'import/resolver': {
			'babel-module': {},
			node: {
				extensions: ['.js', '.jsx', '.json', '.native.js']
			},
			typescript: true
		}
	},
	ignorePatterns: ['android/**/*.js', 'ios/**/*.js'],
	rules: {
		'prettier/prettier': 'off',
		indent: ['error', 'tab', { SwitchCase: 1 }],
		quotes: ['error', 'single'],
		semi: ['error', 'always'],
		'no-multi-spaces': 'error',
		'linebreak-style': ['error', 'unix'],
		'arrow-parens': ['error', 'as-needed'],
		'no-trailing-spaces': ['error'],
		'space-infix-ops': 'error',
		'require-await': 'error',
		'keyword-spacing': 'error',
		'comma-dangle': ['error', 'never'],
		'object-curly-spacing': ['error', 'always'],
		'import/no-unresolved': 'error',
		'import/named': 'error',
		'import/namespace': ['error', { allowComputed: true }],
		'import/default': 'error',
		'import/export': 'error',
		'arrow-body-style': ['error', 'as-needed'],
		'react/jsx-tag-spacing': ['error', {
			beforeClosing: 'never',
			beforeSelfClosing: 'always',
			afterOpening: 'never'
		}],
		'import/order': [
			'error',
			{
				groups: ['builtin', 'external', 'parent', 'sibling', 'index'],
				alphabetize: {
					order: 'asc',
					caseInsensitive: true
				},
				'newlines-between': 'always'
			}
		],
		'prefer-arrow/prefer-arrow-functions': [
			'error',
			{
				disallowPrototype: true,
				singleReturnOnly: true,
				classPropertiesAllowed: false
			}
		],
		'func-style': ['error', 'expression', { allowArrowFunctions: true }],
		'array-element-newline': ['error', 'consistent'],
		'no-bitwise': 'off',
		'no-multiple-empty-lines': ['error', { max: 1, maxBOF: 0, maxEOF: 1 }],
		'react/jsx-boolean-value': 'error',
		'react/jsx-sort-props': [2, {
			callbacksLast: true,
			shorthandFirst: true,
			ignoreCase: true,
			noSortAlphabetically: false,
			reservedFirst: true
		}],
		'react/jsx-wrap-multilines': ['error', {
			declaration: 'parens-new-line',
			assignment: 'parens-new-line',
			return: 'parens-new-line',
			arrow: 'parens-new-line',
			condition: 'parens-new-line',
			logical: 'parens-new-line',
			prop: 'parens-new-line'
		}],
		'react/no-unstable-nested-components': ['warn', { 'allowAsProps': true }],
		'react/jsx-indent': [2, 'tab'],
		'react/jsx-uses-react': 'off',
		'react/react-in-jsx-scope': 'off',
		'no-shadow': 'off',
		'@typescript-eslint/func-call-spacing': 'off'
	}
};

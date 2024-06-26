module.exports = {
    preset: 'jest-preset-angular',
    setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
    testPathIgnorePatterns: ['<rootDir>/node_modules/', '<rootDir>/dist/'],
    globals: {
      'ts-jest': {
        tsconfig: 'tsconfig.spec.json',
        stringifyContentPathRegex: '\\.html$',
      },
    },
    moduleNameMapper: {
      '@app/(.*)': '<rootDir>/src/app/$1',
      '@environments/(.*)': '<rootDir>/src/environments/$1',
    },
    transform: {
      '^.+\\.(ts|html)$': 'ts-jest',
    },
  };
  
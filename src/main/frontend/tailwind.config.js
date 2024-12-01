/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["../resources/templates/**/*.{html,js}", "./resources/static/**/*.js"],
  theme: {
    extend: {},
  },
  plugins: [
    require('flowbite/plugin')
  ]
}


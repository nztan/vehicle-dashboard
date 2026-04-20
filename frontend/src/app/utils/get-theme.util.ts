export const getIconColor = () => {
  return getComputedStyle(document.documentElement)
    .getPropertyValue('--icon-color')
    .trim();
}

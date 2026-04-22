export const getDefaultIconColor = () => {
  return getComputedStyle(document.documentElement)
    .getPropertyValue('--icon-color')
    .trim();
}

export const getChargingColor = () => {
  return getComputedStyle(document.documentElement)
    .getPropertyValue('--charging-color')
    .trim();
}

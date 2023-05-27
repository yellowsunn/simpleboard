function getPageList(currentPage, totalPages, pageSize) {
  const startIdx = Math.floor((currentPage - 1) / pageSize) * pageSize + 1
  const pageList = []
  for (let i = startIdx; i < Math.min(totalPages + 1, startIdx + 5); i++) {
    pageList.push(i)
  }
  return pageList
}

export default getPageList

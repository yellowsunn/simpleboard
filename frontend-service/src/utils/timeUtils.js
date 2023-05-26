import TimeAgo from "javascript-time-ago";

function timeAgoFormat(dateFormat) {
  const now = new Date()
  const diff = now - new Date(dateFormat)
  const timeAgo = new TimeAgo("ko")
  return timeAgo.format(now - diff)
}

export {timeAgoFormat}

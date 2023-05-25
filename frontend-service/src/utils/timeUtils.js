import TimeAgo from "javascript-time-ago";
import ko from "javascript-time-ago/locale/ko";

function timeAgoFormat(dateFormat) {
  TimeAgo.addDefaultLocale(ko)
  const now = new Date()
  const diff = now - new Date(dateFormat)
  const timeAgo = new TimeAgo("ko")
  return timeAgo.format(now - diff)
}

export {timeAgoFormat}

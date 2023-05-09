function rewritePath(r) {
  const width = r.args['width'] || 0
  const height = r.args['height'] || 0

  const s3Uri = btoa(`s3:/${r.uri}`)
  return `insecure/resize:fill:${width}:${height}/gravity:sm/${s3Uri}`
}

export default { rewritePath };

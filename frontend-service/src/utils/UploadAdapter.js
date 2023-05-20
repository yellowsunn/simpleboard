// https://ckeditor.com/docs/ckeditor5/latest/framework/deep-dive/upload-adapter.html

import {getAccessToken} from "@/utils/tokenUtils";

export default class UploadAdapter {
  constructor(loader) {
    // The file loader instance to use during the upload.
    this.loader = loader;
  }

  // Starts the upload process.
  upload() {
    return this.loader.file.then(
      (file) =>
        new Promise((resolve, reject) => {
          this._initRequest();
          this._initListeners(resolve, reject, file);
          this._sendRequest(file);
        })
    );
  }

  // Aborts the upload process.
  abort() {
    if (this.xhr) {
      this.xhr.abort();
    }
  }

  // Initializes the XMLHttpRequest object using the URL passed to the constructor.
  _initRequest() {
    const xhr = this.xhr = new XMLHttpRequest()

    xhr.open("PATCH", `${process.env.VUE_APP_BOARD_API_BASE_URL}/api/v2/users/my-info/thumbnail`, true)
    xhr.setRequestHeader("Authorization", `Bearer ${getAccessToken()}`)
    // xhr.responseType = "json"
  }

  // Initializes XMLHttpRequest listeners.
  _initListeners(resolve, reject, file) {
    const xhr = this.xhr;
    const loader = this.loader;
    const genericErrorText = `Couldn't upload file: ${file.name}.`;

    xhr.addEventListener("error", () => reject(genericErrorText));
    xhr.addEventListener("abort", () => reject());
    xhr.addEventListener("load", () => {
      console.log(xhr)
      const response = xhr.response;
      console.log(response)

      if (!response || response.error) {
        return reject(response && response.error ? response.error.message : genericErrorText);
      }

      resolve({
        default: response.data,
      });
    });

    if (xhr.upload) {
      xhr.upload.addEventListener("progress", (evt) => {
        if (evt.lengthComputable) {
          loader.uploadTotal = evt.total;
          loader.uploaded = evt.loaded;
        }
      });
    }
  }

  _sendRequest(file) {
    // Prepare the form data.
    const data = new FormData();

    // 이미지 파일
    data.append("thumbnail", file);

    // Send the request.
    this.xhr.send(data);
  }

}

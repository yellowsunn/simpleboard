<template>
    <div>
        <CkEditor :editor="editor" :config="editorConfig" v-model="editorContent"/>
    </div>
</template>

<script>
import CKEditor from "@ckeditor/ckeditor5-vue";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import UploadAdapter from "@/utils/UploadAdapter";

export default {
  name: "ArticleWriteEditor",
  components: {'CkEditor': CKEditor.component},
  props: {
    content: String,
  },
  data() {
    return {
      editorContent: "",
      editor: ClassicEditor,
      editorConfig: {
        language: 'ko',
        toolbar: ['heading', '|', 'imageUpload', 'link', '|', 'bold', 'italic', '|', 'bulletedList', 'numberedList', 'indent', 'outdent', '|', 'undo', 'redo'],
        table: {
          contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties'],
        },
        image: {
          resize: true,
          toolbar: ['imageStyle:alignLeft', 'imageStyle:alignRight', 'imageStyle:inline', 'imageStyle:side']
        },
        extraPlugins: [function (editor) {
          editor.plugins.get('FileRepository').createUploadAdapter = (loader) => {
            return new UploadAdapter(loader);
          }
        }],
      }
    }
  },
  mounted() {
    this.editorContent = this.content
  }
}
</script>

<style>
.ck-editor__editable {
    min-height: 300px;
    max-height: 600px;
}
</style>

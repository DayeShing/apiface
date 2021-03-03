var gulp = require('gulp');

gulp.task('precss', async function () {
  await gulp.src('./changelog.md').pipe(gulp.dest("./dist"))
  // await gulp.src('./static/index.html').pipe(gulp.dest("./dist"))
});

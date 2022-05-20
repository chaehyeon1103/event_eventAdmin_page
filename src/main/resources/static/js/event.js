var tag = document.createElement('script')
tag.src = 'https://www.youtube.com/iframe_api'
var firstScriptTag = document.getElementsByTagName('script')[0]
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag)
;(function () {
      var canvas = $('.result')[0]

      var context = canvas.getContext('2d')

      var images = []
      var background
      var currentNicname
      var currentComment
      var currentImageIndex = 0

      var galleryList
      var galleryIndex = 0
      var galleryViewCount = 8
      var ytPlayer

      init()
      function init() {
            for (var i = 1; i <= 9; i++) {
                  images.push('images/p' + i + '.jpg')
            }
            initEvents()
            loadImage()
      }

      function initEvents() {
            $(window)
                  .on('resize', function (e) {
                        var w = $(this).width()
                        var h = $(this).height()
                        var scale = 9 / 16
                        var mh = h * 0.8
                        var mw = mh * scale
                        $('.video').css({
                              width: mw,
                              height: mh,
                        })
                  })
                  .trigger('resize')

            $('.popup_box .close').on('click', function (e) {
                  $(this).closest('.pop').hide()
                  if (ytPlayer) {
                        ytPlayer.pauseVideo()
                  }
                  return false
            })

            $('.youtube a').on('click', function (e) {
                  showMovieLayer()
                  return false
            })

            $('#nicname').on('keyup', function (e) {
                  currentNicname = $(this).val()
                  render()
            })
            $('#txt').on('keyup', function (e) {
                  currentComment = $(this).val()
                  render()
            })

            $('.btn-clipboard').on('click', function (e) {
                  setClipboard('clipboard')
                  return false
            })

            $(window).on('click', function (e) {
                  // console.log(e.target)
                  if (!$(e.target).hasClass('selbox')) {
                        $('.select dl').hide()
                  }
            })

            $('.select .selbox').on('click', function (e) {
                  $(this).siblings('dl').show()
            })

            $('.select dl dd').on('click', function (e) {
                  currentImageIndex = $(this).index()
                  $('.select .selbox').siblings('dl').hide()
                  // $('.select .selbox').text($(this).text())
                  $('.select .selbox').val($(this).text())
                  loadImage()
            })

            $('.btn-complete').on('click', function (e) {
                  if (!$('#nicname').val().length) {
                        alert('아이디를 입력해주세요')
                        return false
                  }
                  if (!$('#txt').val().length) {
                        alert('나만의 탕수육 띵언을 입력해주세요')
                        return false
                  }
                  $('#layer_down').fadeIn()
                  return false
            })

            $('#layer_down .btn2').on('click', function (e) {
                  $('#layer_down').fadeOut()
                  return false
            })

            $('.sns').on('click', function (e) {
                  $('#layer_sns').fadeIn()
                  return false
            })

            $('.close').on('click', function (e) {
                  $('#layer_sns').fadeOut()
                  return false
            })
      }

      function showMovieLayer() {
            $('#layer_video').fadeIn()
            createYTPlayer('ND_bmyDfXV4')
      }

      function createYTPlayer(code) {
            if (ytPlayer) {
                  ytPlayer.loadVideoById({ videoId: code })
                  return
            }
            ytPlayer = new YT.Player('player', {
                  height: $('.video').height(),
                  width: $('.video').width(),
                  videoId: code,
                  playerVars: {
                        autoplay: 1,
                        rel: 0,
                        showinfo: 0,
                        wmode: 'opaque',
                  },
                  events: {
                        onReady: function (e) {
                              ytPlayer.playVideo()
                        },
                  },
            })
      }

      function loadImage() {
            background = new Image()
            background.onload = function () {
                  render()
            }
            background.src = images[currentImageIndex]
      }

      function render() {
            context.drawImage(background, 0, 0)
            context.fillStyle = '#fff'
            // currentNicname = '@sikigima';
            if (currentNicname) {
                  context.font = '16pt Arita-buri-SemiBold'
                  context.fillStyle = '#fffae2'

                  var nic = '- ' + currentNicname + '- '
                  var nicWidth = context.measureText(nic).width
                  var nicX = (canvas.width - nicWidth) * 0.5

                  context.fillText(nic, nicX, 93)
            }
            if (currentComment) {
                  context.font = '25pt Arita-buri-SemiBold'
                  context.fillStyle = '#fff164'
                  context.shadowColor = '#111'
                  context.shadowBlur = 2
                  context.shadowOffsetX = 1
                  context.shadowOffsetY = 1

                  var text = '"' + currentComment + '"'
                  var commentWidth = context.measureText(text).width
                  var commentX = (canvas.width - commentWidth) * 0.5
                  context.fillText(text, commentX, 58)
            }
            download()
      }

      function download() {
            var image = canvas.toDataURL('image/jpeg', 1.0)
            $('#layer_down .popup_box img').attr('src', image)
            $('#layer_down .btn1').attr('href', image)
      }

      function setClipboard(param) {
            var copyText = document.getElementById(param)
            copyText.select()
            document.execCommand('copy')
            alert('클립보드에 복사되었습니다. Ctrl+V로 붙여넣기 하세요')
      }

      //----------------채현------------------

      //파일 종류 및 크기 확인
      var maxSize = 5242880 //5MB
      const checkExtension = (fileName, fileSize) => {
            var pathPoint = fileName.lastIndexOf('.')
            var filePoint = fileName.substring(pathPoint + 1, fileName.length)
            var fileType = filePoint.toLowerCase()

            //파일 종류 확인
            if (fileType != 'jpg' && fileType != 'gif' && fileType != 'png' && fileType != 'jpeg' && fileType != 'bmp') {
                  alert('업로드 하신 파일은 이미지 파일이 아닙니다. \n확인 후 다시 올려주시기 바랍니다.')
                  return false
            }
            //파일 크기 확인
            if (fileSize >= maxSize) {
                  alert('파일 사이즈를 초과하였습니다.')
                  return false
            }
            return true
      }

      //참여완료 버튼 클릭 시
      $('#layer_down .btn3').on('click', function (e) {
            e.preventDefault()

            if ($('#uploadFile').val() == '') {
                  alert('파일을 선택해 주세요.')
                  return false
            }

            var url = $('#addForm').attr('action')
            var formData = new FormData()
            var inputFile = $("input[name='uploadFile']")
            var file = inputFile[0].files[0]

            var id = $('#nicname').val()
            var type = $('#type').val()
            var content = $('#txt').val()

            formData.append('uploadFile', file)
            formData.append('id', id)
            formData.append('type', type)
            formData.append('content', content)

            //파일 종류 및 크기 확인
            if (!checkExtension(file.name, file.size)) {
                  return false
            }

            //ajax 통해 파일 업로드
            $.ajax({
                  enctype: 'multipart/form-data',
                  url: url,
                  processData: false,
                  contentType: false,
                  cache: false,
                  data: formData,
                  type: 'POST',
                  success: function (result) {
                        if ((result = true)) {
                              alert('성공적으로 등록되었습니다.')
                              e.preventDefault()
                              page = 1
                              showImage()
                        } else {
                              alert('다시 시도해주세요')
                        }
                  },
            })
            $('#layer_down').fadeOut()

            $('#nicname').val('')
            $('#txt').val('')
      })

      //이미지 출력 함수
      const showImage = () => {
            $('.uploadResult ul li').remove()

            console.log('showImage 실행')
            var str = ''

            //8개 단위로 모든 img태그 생성
            //초기 8개 noImg 출력
            for (let i = 0; i < 8; i++) {
                  str += "<li><img src='images/noimg.jpg' width='286' class='img" + i + "'></li>"
            }
            $('.uploadResult ul').append(str)

			var page = 1 //현재 페이지
			
            //첫번째 페이지 결과들로 src 변경 후 출력(1page)
            $.ajax({
                  url: '/api/event/indexList?page='+page,
                  async: false,
                  type: 'post',
                  success: function (data) {
                        $.each(data, function (i, item) {
                              console.log(item.fileName)
                              $('.img' + i).attr('src', '/api/event/display?fileName=' + item.fileName)
                        })
                  },
            })
      }
      
	  var page = 1 //현재 페이지
		
      //페이징 함수
      const paging = () => {
            console.log('paging 실행')
            var cnt = 0 //총 게시물 수

            $.ajax({
                  url: '/api/event/getCount',
                  async: false,
                  type: 'get',
                  success: function (data) {
                        cnt = data
                  },
            })

            var totalPage = Math.floor((cnt - 1) / 8) + 1 //최대 페이지 수(더보기를 누를 수 있는 최대 횟수)

            //더보기 클릭 시(이미지 추가 출력)
            $('.more').click(function (e) {
                  e.preventDefault()
                  if (totalPage <= page) {

                        //현재 페이지가 최대 페이지 수보다 크거나 같다면 '로드할 이미지 없다'는 alert 출력
                        page = totalPage

                        alert('더 이상 로드할 이미지가 없습니다')
                  } else {

                        page++ //현재 페이지 수 추가
                        var str = ''

                        $.ajax({
                              url: '/api/event/indexList?page='+page,
                              async: false,
                              type: 'post',
                              success: function (data) {
                                    $.each(data, function (i, item) {
                                          str += "<li><img src='/api/event/display?fileName=" + item.fileName + "' width='286'></li>"
                                    })
                                    
                                    $('.uploadResult ul').append(str)
                                    console.log(str)
                              },
                        })
                  }
            })
      }
      showImage()
      paging()
})()

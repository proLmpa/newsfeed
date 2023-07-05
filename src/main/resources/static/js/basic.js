const host = 'http://' + window.location.host;

$(document).ready(function () {
    const auth = getToken()

    if(auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setReqeustHeader('Authorization', auth)
        });
    } else {
        window.location.href = host + '/api/user/login-page'
        return
    }

    $.ajax({
        type: 'GET',
        url: `/api/user/profile`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const id = res.id
            console.log(id)

            if(!id) {
                window.location.href = '/api/user/login-page'
                return
            }

            $('#id').text(id)
            showPost(); // 게시글 전부 조회하기

            // 좋아요(or 팔로우) 조회하기
        })
        .fail(function(jqXHR, textStatus) {
            logout()
        })
})

function getToken() {
    let auth = Cookies.get('Authorization')

    if(auth === undefined) return ''

    return auth
}

function showPost(postId = null) {
    let dataSource = null

    if(postId) {
        dataSource = `/api/post/${postId}`
    } else {
        dataSource = `/api/post`
    }

    $('#post-container').empty()
    $.ajax({
        type: 'GET',
        url: dataSource,
        contentType: 'application/json',
        ajax: {
            function () {
                $('#post-container').html('게시글 불러오는 중..')
            },
            error(error, status, request) {
                if(error.status === 400) {
                    $('html').html(error.responseText)
                    return
                }
                logout()
            }
        }
    })
}

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'})
    window.location.href = host + '/api/user/login-page'
}
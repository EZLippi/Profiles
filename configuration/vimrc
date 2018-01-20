set rtp+=/usr/local/lib/python2.7/dist-packages/powerline/bindings/vim
set nocompatible              " be iMproved, required
filetype off                  " required
" set the runtime path to include Vundle and initialize
set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()
" alternatively, pass a path where Vundle should install plugins
"call vundle#begin('~/some/path/here')
"
" let Vundle manage Vundle, required
Plugin 'gmarik/Vundle.vim'
Plugin 'Shougo/neocomplete.vim'
" The following are examples of different formats supported.
" Keep Plugin commands between vundle#begin/end.

" plugin on GitHub repo
Plugin 'c.vim'
Plugin 'spf13/vim-autoclose'
""Plugin 'tpope/vim-fugitive'
Plugin 'scrooloose/syntastic'
Plugin 'Lokaltog/vim-easymotion'
Plugin 'tpope/vim-fugitive'
Plugin 'kien/ctrlp.vim'
Plugin 'tpope/vim-surround'
" plugin from http://vim-scripts.org/vim/scripts.html
Plugin 'L9'
" Git plugin not hosted on GitHub
Plugin 'git://git.wincent.com/command-t.git'
"VIM markdown highlight
Plugin 'godlygeek/tabular'
Plugin 'plasticboy/vim-markdown'
" git repos on your local machine (i.e. when working on your own plugin)
""Plugin 'file:///home/gmarik/path/to/plugin'
" The sparkup vim script is in a subdirectory of this repo called vim.
" Pass the path to set the runtimepath properly.
Plugin 'rstacruz/sparkup', {'rtp': 'vim/'}
" Avoid a name conflict with L9
""Plugin 'user/L9', {'name': 'newL9'}
"class PluginInstall
" All of your Plugins must be added before the following line
call vundle#end()            " required
filetype plugin indent on    " required
" To ignore plugin indent changes, instead use:
"filetype plugin on
"
" Brief help
" :PluginList       - lists configured plugins
" :PluginInstall    - installs plugins; append `!` to update or just :PluginUpdate
" :PluginSearch foo - searches for foo; append `!` to refresh local cache
" :PluginClean      - confirms removal of unused plugins; append `!` to auto-approve removal
"
" see :h vundle for more details or wiki for FAQ
" Put your non-Plugin stuff after this line

"source $VIMRUNTIME/vimrc_example.vim
"source $VIMRUNTIME/mswin.vim
"behave mswin
" 设置gvim启动窗口的位置，以及大小   
"winpos 100 500  
set lines=35 columns=100  
" 设置gvim的字体和大小，以及配色方案
" 字体设置和Linux稍微有点区别，请使用冒号隔开，或者使用双引号括起来，而不是Linux下的空格转义
"set guifont=Lucida_Console:14:cANSI

"设置配色方案
colorscheme bandit
set t_Co=256
"设置语法高亮
syntax on
syntax enable 
set tabstop=4		""设置tab间隔
set fdm=marker  	"折叠
set softtabstop=4
set shiftwidth=4
set autoindent		"设置自动换行"
set cindent			"设置c风格换行
set smartindent
set smarttab
set nu				"显示行号"
set hlsearch		"设置高亮搜索"
set showmatch		"设置匹配模式
set incsearch       "输入字符串就显示匹配点
set cinoptions={0,1s,t0,n-2,p2s,(03s,=.5s,=1s,:1s
if &term=="xterm"
set t_Co=8
set t_Sb=^[[4%dm
set t_Sf=^[[3%dm
endif
filetype on		"侦测文件类型
filetype plugin on
filetype indent on
set viminfo+=!	"保存全局变量
set nobackup		"禁止生成临时
set noswapfile		"禁止生成swap文件"
set gdefault		"总是显示状态行
set laststatus=2	
set backspace=2		"使退格建正常处理indent,eol
"可以在buffer任何地方使用鼠标
set mouse=a
set selection=exclusive
set selectmode=mouse,key
"通过使用:command命令告诉我们哪一行被修改过
"set report=0
set wildmenu		"增强模式中的命令行自动完成操作
set cmdheight=2   "" 命令行（在状态行下）的高度，默认为1，这里是2
""//代码补全
set completeopt=preview,menu 
set showmatch	"高亮显示匹配的括号
set matchtime=5
set ignorecase	"搜索时忽视大小写
set scrolloff=3 "光标移动到顶部与底部保持三行距离"
filetype plugin indent on 
filetype on

"--命令行设置--
set showcmd " 命令行显示输入的命令
set showmode " 命令行显示vim当前模式


""//"自动补全
:inoremap ( ()<ESC>i
:inoremap ) <c-r>=ClosePair(')')<CR>
:inoremap { {<CR>}<ESC>O
:inoremap } <c-r>=ClosePair('}')<CR>
:inoremap [ []<ESC>i
:inoremap ] <c-r>=ClosePair(']')<CR>
:inoremap " ""<ESC>i
":inoremap " <c-r>=ClosePair('"')<CR>
:inoremap ' ''<ESC>i
":inoremap ' <c-r>=ClosePair('\'')<CR>
function! ClosePair(char)
if getline('.')[col('.') - 1] == a:char
return "\<Right>"
else
return a:char
endif
endfunction

"markdown settings"
let g:vim_markdown_folding_disable=1
let g:vim_markdown_no_default_key_mappings=1
let g:vim_markdown_math=1
let g:vim_markdown_frontmatter=1
set nofoldenable
"Tlist settings"
let Tlist_Show_One_File=1 
let Tlist_Exit_OnlyWindow=1 
let Tlist_Process_File_Always=1
let g:winManagerWindowLayout='FileExplorer|TagList' 
nmap wm :WMToggle<cr> 
let g:miniBufExplMapCTabSwitchBufs=1 
let g:miniBufExplMapWindowsNavVim=1 
let g:miniBufExplMapWindowNavArrows=1 
nnoremap <silent> <F12> :A<CR> 
nnoremap <silent> <F3> :Grep<CR> 

"quickfix seeting"
nmap cc :cc<cr>
nmap cn :cn<cr>
nmap cp :cp<cr>
nmap cw :cw<cr>

"解决中文乱码
set encoding=utf-8 
set termencoding=utf-8 
set fileencodings=utf-8,chinese,latin-1 
if has("win32") 
set fileencoding=chinese 
else 
set fileencoding=utf-8 
endif 
language messages zh_CN.utf-8

"ctags配置
set tags=tags
set autochdir 
" windows ctags 索引文件 
"set tags+=C:\ctags\cpp_src/cpp
" 按下F5重新生成tag文件，并更新taglist
map <F5> :!ctags -R --c++-kinds=+p --fields=+iaS --extra=+q .<CR><CR> :TlistUpdate<CR>
imap <F5> <ESC>:!ctags -R --c++-kinds=+p --fields=+iaS --extra=+q .<CR><CR> :TlistUpdate<CR>

" OmniCppComplete
let OmniCpp_NamespaceSearch = 1
let OmniCpp_GlobalScopeSearch = 1
let OmniCpp_ShowAccess = 1
let OmniCpp_ShowPrototypeInAbbr = 1 " 显示函数参数列表
let OmniCpp_MayCompleteDot = 1   " 输入 .  后自动补全
let OmniCpp_MayCompleteArrow = 1 " 输入 -> 后自动补全
let OmniCpp_MayCompleteScope = 1 " 输入 :: 后自动补全
let OmniCpp_DefaultNamespaces = ["std", "_GLIBCXX_STD"]
" 自动关闭补全窗口
au CursorMovedI,InsertLeave * if pumvisible() == 0|silent! pclose|endif
set completeopt=menuone,menu,longest

"easymotion配置
"搜索两个字符 
 nmap s <Plug>(easymotion-s2)
 nmap t <Plug>(easymotion-t2)
 "搜索n个字符
 map  / <Plug>(easymotion-sn)
 omap / <Plug>(easymotion-tn)
"移动到上一个和下一个搜索结果
  map  n <Plug>(easymotion-next)
  map  N <Plug>(easymotion-prev)
  "大小写不敏感
  let g:EasyMotion_smartcase = 1

  "neocomplete
  let g:neocomplete#enable_at_startup = 1
"Note: This option must set it in .vimrc(_vimrc).  NOT IN .gvimrc(_gvimrc)!
" Disable AutoComplPop.
let g:acp_enableAtStartup = 0
" Use neocomplete.
let g:neocomplete#enable_at_startup = 1
" Use smartcase.
let g:neocomplete#enable_smart_case = 1
" Set minimum syntax keyword length.
let g:neocomplete#sources#syntax#min_keyword_length = 3
let g:neocomplete#lock_buffer_name_pattern = '\*ku\*'

" Define dictionary.
let g:neocomplete#sources#dictionary#dictionaries = {
    \ 'default' : '',
    \ 'vimshell' : $HOME.'/.vimshell_hist',
    \ 'scheme' : $HOME.'/.gosh_completions'
        \ }

" Define keyword.
if !exists('g:neocomplete#keyword_patterns')
    let g:neocomplete#keyword_patterns = {}
endif
let g:neocomplete#keyword_patterns['default'] = '\h\w*'

" Plugin key-mappings.
inoremap <expr><C-g>     neocomplete#undo_completion()
inoremap <expr><C-l>     neocomplete#complete_common_string()

" Recommended key-mappings.
" <CR>: close popup and save indent.
inoremap <silent> <CR> <C-r>=<SID>my_cr_function()<CR>
function! s:my_cr_function()
  return neocomplete#close_popup() . "\<CR>"
  " For no inserting <CR> key.
  "return pumvisible() ? neocomplete#close_popup() : "\<CR>"
endfunction
" <TAB>: completion.
inoremap <expr><TAB>  pumvisible() ? "\<C-n>" : "\<TAB>"
" <C-h>, <BS>: close popup and delete backword char.
inoremap <expr><C-h> neocomplete#smart_close_popup()."\<C-h>"
inoremap <expr><BS> neocomplete#smart_close_popup()."\<C-h>"
inoremap <expr><C-y>  neocomplete#close_popup()
inoremap <expr><C-e>  neocomplete#cancel_popup()
" Close popup by <Space>.
"inoremap <expr><Space> pumvisible() ? neocomplete#close_popup() : "\<Space>"

" For cursor moving in insert mode(Not recommended)
"inoremap <expr><Left>  neocomplete#close_popup() . "\<Left>"
"inoremap <expr><Right> neocomplete#close_popup() . "\<Right>"
"inoremap <expr><Up>    neocomplete#close_popup() . "\<Up>"
"inoremap <expr><Down>  neocomplete#close_popup() . "\<Down>"
" Or set this.
"let g:neocomplete#enable_cursor_hold_i = 1
" Or set this.
"let g:neocomplete#enable_insert_char_pre = 1

" AutoComplPop like behavior.
"let g:neocomplete#enable_auto_select = 1

" Shell like behavior(not recommended).
"set completeopt+=longest
"let g:neocomplete#enable_auto_select = 1
"let g:neocomplete#disable_auto_complete = 1
"inoremap <expr><TAB>  pumvisible() ? "\<Down>" : "\<C-x>\<C-u>"

" Enable omni completion.
autocmd FileType css setlocal omnifunc=csscomplete#CompleteCSS
autocmd FileType html,markdown setlocal omnifunc=htmlcomplete#CompleteTags
autocmd FileType javascript setlocal omnifunc=javascriptcomplete#CompleteJS
autocmd FileType python setlocal omnifunc=pythoncomplete#Complete
autocmd FileType xml setlocal omnifunc=xmlcomplete#CompleteTags

" Enable heavy omni completion.
if !exists('g:neocomplete#sources#omni#input_patterns')
  let g:neocomplete#sources#omni#input_patterns = {}
endif
"let g:neocomplete#sources#omni#input_patterns.php = '[^. \t]->\h\w*\|\h\w*::'
"let g:neocomplete#sources#omni#input_patterns.c = '[^.[:digit:] *\t]\%(\.\|->\)'
"let g:neocomplete#sources#omni#input_patterns.cpp = '[^.[:digit:] *\t]\%(\.\|->\)\|\h\w*::'

" For perlomni.vim setting.
" https://github.com/c9s/perlomni.vim
let g:neocomplete#sources#omni#input_patterns.perl = '\h\w*->\h\w*\|\h\w*::'

set statusline+=%#warningmsg#
set statusline+=%{SyntasticStatuslineFlag()}
set statusline+=%*

let g:syntastic_always_populate_loc_list = 1
let g:syntastic_auto_loc_list = 1
let g:syntastic_check_on_open = 1
let g:syntastic_check_on_wq = 0
let g:syntastic_c_check_header = 1

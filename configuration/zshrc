# Path to your oh-my-zsh installation.
export ZSH=/home/lippi/.oh-my-zsh
export EDITOR='vim'
# Set name of the theme to load.
# Look in ~/.oh-my-zsh/themes/
# Optionally, if you set this to "random", it'll load a random theme each
# time that oh-my-zsh is loaded.
ZSH_THEME="robbyrussell"

PROMPT='%{$fg_bold[red]%}➜ %{$fg_bold[green]%}%p%{$fg[cyan]%}%d %{$fg_bold[blue]%}$(git_prompt_info)%{$fg_bold[blue]%}% %{$reset_color%}>'

[[ -s ~/.autojump/etc/profile.d/autojump.sh ]] && . ~/.autojump/etc/profile.d/autojump.sh
#auto execute powerline 
. /usr/local/lib/python2.7/dist-packages/powerline/bindings/zsh/powerline.zsh
# Uncomment the following line to use case-sensitive completion.
# CASE_SENSITIVE="true"

# Uncomment the following line to use hyphen-insensitive completion. Case
# sensitive completion must be off. _ and - will be interchangeable.
# HYPHEN_INSENSITIVE="true"

# Uncomment the following line to disable bi-weekly auto-update checks.
# DISABLE_AUTO_UPDATE="true"

# Uncomment the following line to change how often to auto-update (in days).
# export UPDATE_ZSH_DAYS=13

# Uncomment the following line to disable colors in ls.
# DISABLE_LS_COLORS="true"

# Uncomment the following line to disable auto-setting terminal title.
# DISABLE_AUTO_TITLE="true"

# Uncomment the following line to enable command auto-correction.
# ENABLE_CORRECTION="true"

# Uncomment the following line to display red dots whilst waiting for completion.
 COMPLETION_WAITING_DOTS="true"

# Uncomment the following line if you want to disable marking untracked files
# under VCS as dirty. This makes repository status check for large repositories
# much, much faster.
# DISABLE_UNTRACKED_FILES_DIRTY="true"

# Uncomment the following line if you want to change the command execution time
# stamp shown in the history command output.
# The optional three formats: "mm/dd/yyyy"|"dd.mm.yyyy"|"yyyy-mm-dd"
 HIST_STAMPS="yyyy-mm-dd"

# Would you like to use another custom folder than $ZSH/custom?
# ZSH_CUSTOM=/path/to/new-custom-folder

# Which plugins would you like to load? (plugins can be found in ~/.oh-my-zsh/plugins/*)
# Custom plugins may be added to ~/.oh-my-zsh/custom/plugins/
# Example format: plugins=(rails git textmate ruby lighthouse)
# Add wisely, as too many plugins slow down shell startup.
plugins=(git textmate ruby autojump  mvn gradle)

# User configuration


eval "$(thefuck-alias)"
# You can use whatever you want as an alias, like for Mondays:
eval "$(thefuck-alias FUCK)"
export PATH="/home/lippi/Downloads/goagent-goagent-98eca96/local:/opt/genymotion-2.4.0_x64.bin:/opt/idea-IU-141.178.9/bin:/opt/gradle-2.3/bin:/usr/local/texlive/2013/bin/x86_64-linux:/opt/eclipse:/opt/android-studio/bin:/opt/jdk1.8.0_31/bin:/opt/jdk1.8.0_31/jre/bin:/home/lippi/Downloads/goagent-goagent-98eca96/local:/opt/genymotion-2.4.0_x64.bin:/opt/idea-IU-141.178.9/bin:/opt/gradle-2.3/bin:/usr/local/texlive/2013/bin/x86_64-linux:/opt/eclipse:/opt/android-studio/bin:/opt/jdk1.8.0_31/bin:/opt/jdk1.8.0_31/jre/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/home/lippi/Downloads/android-ndk-r10d:/home/lippi/Downloads/android-ndk-r10d"
# export MANPATH="/usr/local/man:$MANPATH"
HISTSIZE=5000
HISTFILESIZE=5000
export HISTTIMEFORMAT="%d/%m/%y %T  "


# You may need to manually set your language environment
# export LANG=en_US.UTF-8

# Preferred editor for local and remote sessions
# if [[ -n $SSH_CONNECTION ]]; then
#   export EDITOR='vim'
# else
#   export EDITOR='mvim'
# fi

# Compilation flags
# export ARCHFLAGS="-arch x86_64"

# ssh
# export SSH_KEY_PATH="~/.ssh/dsa_id"

# Set personal aliases, overriding those provided by oh-my-zsh libs,
# plugins, and themes. Aliases can be placed here, though oh-my-zsh
# users are encouraged to define aliases within the ZSH_CUSTOM folder.
# For a full list of active aliases, run `alias`.
#
# Example aliases
# alias zshconfig="mate ~/.zshrc"
# alias ohmyzsh="mate ~/.oh-my-zsh"
#
alias cls='clear'
alias ll='ls -alF'
alias la='ls -a'
alias vi='vim'
alias javac="javac -J-Dfile.encoding=utf8"
alias grep="grep --color=auto"
alias -s html=mate   # 在命令行直接输入后缀为 html 的文件名，会在 TextMate 中打开
alias -s rb=mate     # 在命令行直接输入 ruby 文件，会在 TextMate 中打开
alias -s py=vi       # 在命令行直接输入 python 文件，会用 vim 中打开，以下类似
alias -s js=vi
alias -s c=vi
alias -s java=vi
alias -s txt=vi
alias -s gz='tar -xzvf'
alias -s tgz='tar -xzvf'
alias -s zip='unzip'
alias -s bz2='tar -xjvf'
export JAVA_HOME=/opt/jdk1.8.0_31
export JRE_HOME=/opt/jdk1.8.0_31/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export ANDROID_HOME=/opt/sdk
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
export PATH=/opt/android-studio/bin:$PATH
export PATH=/opt/eclipse:$PATH
export TOMCAT_HOME=/opt/apache-tomcat-8.0.11
export PATH=$PATH:/home/lippi/Downloads/android-ndk-r10d
export PATH=/usr/local/texlive/2013/bin/x86_64-linux:$PATH
MANPATH=/usr/local/texlive/2013/texmf-dist/doc/man:$MANPATH;
export MANPATH
INFOPATH=/usr/local/texlive/2013/texmf-dist/doc/info:$INFOPATH;
export INFOPATH
export GRADLE_HOME=/opt/gradle-2.3
export PATH=/opt/gradle-2.3/bin:$PATH
export PATH=/opt/idea-IU-141.178.9/bin:$PATH


source $ZSH/oh-my-zsh.sh


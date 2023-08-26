function browser_run(){
	firefox http://localhost:3000
}


function moveto_target_path(){
	#获取运行的程序名
	PRONAME=`basename $0`
	#获取文件运行的当前目录
	CURPATH=$(cd "$(dirname "$0")"; pwd)
	cd $CURPATH
	cd app
}


moveto_target_path
# browser_run			&
npm start			&
wait


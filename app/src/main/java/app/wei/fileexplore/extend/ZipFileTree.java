package app.wei.fileexplore.extend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import app.wei.fileexplore.Tools.ID;

/**
 * Created by wei on 2017/7/20.
 */

public class ZipFileTree{
        private String path;
        private int type;

        HashMap<String,ZipFileTree> map;
        public ZipFileTree(){
            type= ID.FILE_ZIP_ROOT;
            path="/";
            map=new HashMap<>();

        }
        public ZipFileTree(int type,String path){
            this.type=type;
            this.path=path;
            map=new HashMap<>();
        }

        public void add(String path,int type){
            String[] paths=path.split("/");
            int i=0;
            HashMap findMap=map;
            StringBuilder temp= new StringBuilder(paths[0]);
            while (findMap.containsKey(temp.toString())){
                findMap=((ZipFileTree)findMap.get(temp.toString())).map;
                if (++i<paths.length)
                    temp.append("/").append(paths[i]);
            }

            for (int j = i; j < paths.length; j++) {
                ZipFileTree tree=new ZipFileTree(type, temp.toString());
                findMap.put(temp.toString(),tree);
                findMap=tree.map;
                if (j<paths.length-1)
                    temp.append("/").append(paths[j+1]);
            }
        }

        public List<String> getPathList(String parent){
            String[] paths=parent.split("/");
            ArrayList<String> list=new ArrayList<>();
            int i=0;
            HashMap findMap=map;
            if (paths.length!=0) {
                StringBuilder builder = new StringBuilder(paths[i]);
                while (findMap.containsKey(builder.toString())) {
                    findMap = ((ZipFileTree) findMap.get(builder.toString())).map;
                    i++;
                    if (i < paths.length)
                        builder.append("/").append(paths[i]);
                }
            }
            ArrayList<ZipFileTree> childList=new ArrayList<ZipFileTree>(findMap.values());
            for (ZipFileTree tree:childList){
                list.add(tree.path);
            }
            return list;
        }


        public List<String> toList(String root){
            List<String> lists=new ArrayList<>();
            Stack<String> stack=new Stack<>();
            stack.push(root);
            while (!stack.empty()){
                String path=stack.pop();
                List<String> list=getPathList(path);
                if (list.size()!=0){
                    lists.addAll(list);
                    System.out.println(list);
                    for (String s:list){
                        stack.push(s);
                    }
                }
            }
            return lists;
        }

        public List<List<String>> toSeparatorList(String root){
            List<List<String>> lists=new ArrayList<>();
            Stack<String> stack=new Stack<>();
            stack.push(root);
            while (!stack.empty()){
                String path=stack.pop();
                List<String> list=getPathList(path);
                if (list.size()!=0){
                    lists.add(list);
                    System.out.println(list);
                    for (String s:list){
                        stack.push(s);
                    }
                }
            }
            return lists;
        }

}

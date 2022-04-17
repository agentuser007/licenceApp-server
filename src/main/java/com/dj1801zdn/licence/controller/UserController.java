package com.dj1801zdn.licence.controller;

import com.dj1801zdn.licence.mapper.UserMapper;
import com.dj1801zdn.licence.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Random;

@RestController
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;

    User user = new User();

    @RequestMapping("/test")
    public String test(){
        String sql = "select * from user";
        List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
        return list.toString();
    }

    @RequestMapping("login/{name}/{password}")
    public String login(@PathVariable("name")String name,
                        @PathVariable("password")String password){
        System.out.println("login访问");
        user = userMapper.login(name);
        String result = "";
        if(user == null) {
            result = "wrong name";
        } else if(user.getPassword().equals(password)){
            if(user.getLicence() != null){
                result = "车牌号："+user.getLicence()+"；种类："+user.getType();
            }else{
                result = "0";
            }
        }else{
            result = "wrong password";
        }
        System.out.println(user);
        return result;
    }

    @RequestMapping("signin/{name}/{password}")
    public String signin(@PathVariable("name")String name,
                        @PathVariable("password")String password){
        System.out.println("signin访问");
        user = userMapper.login(name);
        String result = "";
        if(user == null) {
            user = userMapper.signin(name,password);
            result = "sign in";
        } else {
            result = "username exist";}

        System.out.println(user);
        return result;
    }

    @RequestMapping("userquery/{type}")
    public String userquery(@PathVariable("type")String type){
        String sql = "";
        if ("f".equals(type)) {
            sql = "SELECT licence FROM licence.licence where type = '燃油' and occupy is null order by rand() limit 10";
            user.setType("燃油");
        }else {
            sql = "SELECT licence FROM licence.licence where type = '电动' and occupy is null order by rand() limit 10";
            user.setType("电动");
        }
        List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
        return list.toString();
    }

    @RequestMapping("userselect/{licence}")
    public String userselect(@PathVariable("licence")String licence) {
        System.out.println("serselect访问"+licence);

        user.setLicence(licence);
        String sql = "UPDATE licence.licence SET occupy = 1 WHERE licence ='" + licence + "'";
        int a = jdbcTemplate.update(sql);
        userMapper.userselect(user);
        return "selected";
    }


    @RequestMapping("adminquery")
    public String query(){
        String sql = "SELECT licence.id,licence.licence,user.name,licence.type FROM licence.licence left join licence.user on user.licence=licence.licence;";
        List<Map<String, Object>> list= jdbcTemplate.queryForList(sql);
        return list.toString();

    }

    @RequestMapping(value = {"/generate/{a1}/{a2}/{b1}/{b2}/{c1}/{c2}/{d1}/{d2}/{e1}/{e2}/{f1}/{f2}",
            "/generate/{a1}/{a2}/{b1}/{b2}/{c1}/{c2}/{d1}/{d2}/{e1}/{e2}"})
    public String generate(@PathVariable("a1")char a1,@PathVariable("a2")char a2,
                           @PathVariable("b1")char b1,@PathVariable("b2")char b2,
                           @PathVariable("c1")char c1,@PathVariable("c2")char c2,
                           @PathVariable("d1")char d1,@PathVariable("d2")char d2,
                           @PathVariable("e1")char e1,@PathVariable("e2")char e2,
                           @PathVariable(value = "f1",required = false)String f1,
                           @PathVariable(value = "f2",required = false)String f2){
        HashSet<String> set = new HashSet<String>();
        Random rand = new Random();
        while (set.size()<20){
            //int randNumber =rand.nextInt(MAX - MIN + 1) + MIN;
            char a = (char) ((char)rand.nextInt((int)a2 -(int)a1 + 1)+(int)a1);
            char b = (char) ((char)rand.nextInt((int)b2 -(int)b1 + 1)+(int)b1);
            char c = (char) ((char)rand.nextInt((int)c2 -(int)c1 + 1)+(int)c1);
            char d = (char) ((char)rand.nextInt((int)d2 -(int)d1 + 1)+(int)d1);
            char e = (char) ((char)rand.nextInt((int)e2 -(int)e1 + 1)+(int)e1);
            String string = "京"+"A"+a+b+c+d+e;
            if (f1 != null && f2 != null){
                char f = (char) ((char)rand.nextInt((int)f2.charAt(0) -(int)f1.charAt(0) + 1)+(int)f1.charAt(0));
                string += f;
                userMapper.generate(string,"电动");
            }else{
                userMapper.generate(string,"燃油");
            }
            set.add(string);
            System.out.println(set);
        }
        return set.toString();
    }



}

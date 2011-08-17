/*
 * Copyright (C) 2011 Samuli Penttilä <samuli.penttila@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fi.capeismi.fish.uistelupaivakirja.web.dao;

import fi.capeismi.fish.uistelupaivakirja.web.model.RestfulException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Samuli Penttilä <samuli.penttila@gmail.com>
 */
public abstract class View {
    private List<String> _columns = new ArrayList<String>();
    
    public final void addColumn(String colname) {
        this._columns.add(colname);
    }
    
    public final List<String> getColumns() {
        return Collections.unmodifiableList(this._columns);
    }
    
    abstract void add(Map<String, String> row);
       
    public static class ViewFactory {
        public static View getInstance(String view) {
            
            Properties props = new Properties();
            try {
                ApplicationContext ctx = new ClassPathXmlApplicationContext();
                
                props.load(new FileInputStream(ctx.getResource("classpath:fi/capeismi/fish/uistelupaivakirja/web/dao/views.properties").getFile()));
                String classname = props.getProperty(view);
                return (View)Class.forName(classname).newInstance();
                    
            } catch (Exception e) {
                throw new RestfulException(e);
            }            
        }
    }

}

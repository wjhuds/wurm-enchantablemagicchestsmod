package org.booski.wurmunlimited.mods.enchantablemagicchestsmod;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class enchantablemagicchestsmod implements WurmServerMod, PreInitable {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public String getVersion() {
		return "v0.1";
	}

	@Override
	public void preInit() {
		try {
			try {
				ClassPool classPool = HookManager.getInstance().getClassPool();
				classPool.getCtClass("com.wurmonline.server.spells.Spell")
						 .getMethod("run", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;F)Z")
						 .instrument(new ExprEditor() {
							 @Override
							 public void edit(MethodCall m) throws CannotCompileException {
								 if (m.getMethodName().equals("isMagicContainer")) m.replace("$_=false;");
							 }
						 });
				logger.log(Level.INFO, "Successfully allowed magic chest enchantment");
			} catch (CannotCompileException e) {
				e.printStackTrace();
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}
}

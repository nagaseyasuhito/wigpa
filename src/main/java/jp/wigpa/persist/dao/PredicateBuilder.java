package jp.wigpa.persist.dao;

import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class PredicateBuilder {
	public static interface And<T> extends Not<T>, List<T> {
	}

	public static interface Between<T extends Comparable<? super T>> extends Not<T> {
		T getFrom();

		T getTo();

		void setFrom(T from);

		void setTo(T to);
	}

	public static interface In<T> extends Not<T>, List<T> {
	}

	public static interface Not<T> {
		boolean isNot();

		void setNot(boolean isNot);
	}

	public static interface Null<T> extends Not<T> {
		boolean isNull();

		void setNull(boolean isNull);
	}

	public static interface Or<T> extends Not<T>, List<T> {
	}

	private static final ClassPool CLASS_POOL = ClassPool.getDefault();

	private static void addBetweenMethods(CtClass entityClass) throws CannotCompileException, NotFoundException {
		entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(Between.class.getName()));

		entityClass.addField(CtField.make("private java.lang.Comparable from;", entityClass));
		entityClass.addField(CtField.make("private java.lang.Comparable to;", entityClass));

		entityClass.addMethod(CtMethod.make("public void setFrom(java.lang.Comparable from) { this.from = from; }", entityClass));
		entityClass.addMethod(CtMethod.make("public void setTo(java.lang.Comparable to) { this.to = to; }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.lang.Comparable getFrom() { return this.from; }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.lang.Comparable getTo() { return this.to; }", entityClass));
	}

	private static void addListDelegateMethods(CtClass entityClass, Class<?> collectionClass) throws CannotCompileException, NotFoundException {
		entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(collectionClass.getName()));

		CtConstructor constructor = new CtConstructor(new CtClass[] {}, entityClass);
		constructor.setBody(";");
		entityClass.addConstructor(constructor);

		entityClass.addField(CtField.make("private java.util.List values = new java.util.ArrayList();", entityClass));

		entityClass.addMethod(CtMethod.make("public boolean add(Object e) { return this.values.add(e); }", entityClass));
		entityClass.addMethod(CtMethod.make("public void add(int index, Object element) { this.values.add(index, element); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean addAll(java.util.Collection c) { return this.values.addAll(c); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean addAll(int index, java.util.Collection c) { return this.values.addAll(index, c); }", entityClass));
		entityClass.addMethod(CtMethod.make("public void clear() { this.values.clear(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean contains(Object o) { return this.values.contains(o); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean containsAll(java.util.Collection c) { return this.values.containsAll(c); }", entityClass));
		entityClass.addMethod(CtMethod.make("public Object get(int index) { return this.values.get(index); }", entityClass));
		entityClass.addMethod(CtMethod.make("public int indexOf(Object o) { return this.values.indexOf(o); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean isEmpty() { return this.values.isEmpty(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.util.Iterator iterator() { return this.values.iterator(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public int lastIndexOf(Object o) { return this.values.lastIndexOf(o); }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.util.ListIterator listIterator() { return this.values.listIterator(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.util.ListIterator listIterator(int index) { return this.values.listIterator(index); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean remove(Object o) { return this.values.remove(o); }", entityClass));
		entityClass.addMethod(CtMethod.make("public Object remove(int index) { return this.values.remove(index); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean removeAll(java.util.Collection c) { return this.values.removeAll(c); }", entityClass));
		entityClass.addMethod(CtMethod.make("public boolean retainAll(java.util.Collection c) { return this.values.retainAll(c); }", entityClass));
		entityClass.addMethod(CtMethod.make("public Object set(int index, Object element) { return this.values.set(index, element); }", entityClass));
		entityClass.addMethod(CtMethod.make("public int size() { return this.values.size(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public java.util.List subList(int fromIndex, int toIndex) { return this.values.subList(fromIndex, toIndex); }", entityClass));
		entityClass.addMethod(CtMethod.make("public Object[] toArray() { return this.values.toArray(); }", entityClass));
		entityClass.addMethod(CtMethod.make("public Object[] toArray(Object[] a) { return this.values.toArray(a); }", entityClass));
	}

	private static void addNotMethods(CtClass entityClass) throws CannotCompileException, NotFoundException {
		entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(Not.class.getName()));

		entityClass.addField(CtField.make("private boolean isNot;", entityClass));

		entityClass.addMethod(CtMethod.make("public boolean isNot() { return this.isNot; }", entityClass));
		entityClass.addMethod(CtMethod.make("public void setNot(boolean isNot) { this.isNot = isNot; }", entityClass));
	}

	private static void addNullMethods(CtClass entityClass) throws CannotCompileException, NotFoundException {
		entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(Null.class.getName()));

		entityClass.addField(CtField.make("private boolean isNull;", entityClass));

		entityClass.addMethod(CtMethod.make("public boolean isNull() { return this.isNull; }", entityClass));
		entityClass.addMethod(CtMethod.make("public void setNull(boolean isNull) { this.isNull = isNull; }", entityClass));
	}

	/**
	 * {@code and}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> And<T> and(Class<T> clazz) {
		return (And<T>) PredicateBuilder.build(clazz, And.class);
	}

	/**
	 * {@code between}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T extends Comparable<? super T>> Between<T> between(Class<T> clazz) {
		try {
			CtClass entityClass;
			String className = PredicateBuilder.class.getName() + "$" + clazz.getName() + "Between";
			if (clazz.isInterface()) {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className);
				entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(clazz.getName()));
			} else {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className, PredicateBuilder.CLASS_POOL.get(clazz.getCanonicalName()));
			}

			PredicateBuilder.addNotMethods(entityClass);
			PredicateBuilder.addBetweenMethods(entityClass);

			return PredicateBuilder.newInstance(entityClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T build(Class<T> clazz, Class<?> collectionClass) {
		try {
			CtClass entityClass;
			String className = PredicateBuilder.class.getName() + "$" + clazz.getName() + collectionClass.getSimpleName();
			if (clazz.isInterface()) {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className);
				entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(clazz.getName()));
			} else {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className, PredicateBuilder.CLASS_POOL.get(clazz.getCanonicalName()));
			}

			PredicateBuilder.addNotMethods(entityClass);
			PredicateBuilder.addListDelegateMethods(entityClass, collectionClass);

			return PredicateBuilder.<T> cast(PredicateBuilder.newInstance(entityClass));
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T cast(Object object) {
		return (T) object;
	}

	/**
	 * {@code in}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> In<T> in(Class<T> clazz) {
		return (In<T>) PredicateBuilder.build(clazz, In.class);
	}

	@SuppressWarnings("unchecked")
	private static <T> T newInstance(CtClass ctClass) throws InstantiationException, IllegalAccessException, CannotCompileException {
		return (T) ctClass.toClass().newInstance();
	}

	/**
	 * {@code not}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> Not<T> not(Class<T> clazz) {
		try {
			CtClass entityClass;
			String className = PredicateBuilder.class.getName() + "$" + clazz.getName() + "Not";
			if (clazz.isInterface()) {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className);
				entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(clazz.getName()));
			} else {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className, PredicateBuilder.CLASS_POOL.get(clazz.getCanonicalName()));
			}

			PredicateBuilder.addNotMethods(entityClass);

			return PredicateBuilder.newInstance(entityClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@code is null}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> Null<T> nullable(Class<T> clazz) {
		try {
			CtClass entityClass;
			String className = PredicateBuilder.class.getName() + "$" + clazz.getName() + "Null";
			if (clazz.isInterface()) {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className);
				entityClass.addInterface(PredicateBuilder.CLASS_POOL.get(clazz.getName()));
			} else {
				entityClass = PredicateBuilder.CLASS_POOL.makeClass(className, PredicateBuilder.CLASS_POOL.get(clazz.getCanonicalName()));
			}

			PredicateBuilder.addNotMethods(entityClass);
			PredicateBuilder.addNullMethods(entityClass);

			return PredicateBuilder.newInstance(entityClass);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (CannotCompileException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@code or}用の{@code T}オブジェクトを返します。
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Or<T> or(Class<T> clazz) {
		return (Or<T>) PredicateBuilder.build(clazz, Or.class);
	}
}

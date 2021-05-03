package org.hurricanegames.pluginlib.utils.types;

import java.util.Optional;
import java.util.function.Supplier;

import org.hurricanegames.pluginlib.utils.ReflectionUtils;

public class Result<T> {

	public static <T> Result<T> success(T value) {
		return new Result<>(value, null);
	}

	public static <T> Result<T> error(Throwable error) {
		return new Result<>(null, error);
	}

	private final T object;
	private final Throwable throwable;

	public Result(T object, Throwable throwable) {
		this.object = object;
		this.throwable = throwable;
	}

	public boolean isSuccess() {
		return throwable == null;
	}

	public Optional<T> asOptional() {
		rethrow();
		return Optional.ofNullable(object);
	}

	public T get() {
		rethrow();
		return object;
	}

	public T getOrDefault(Supplier<T> defaultSuplier) {
		if (isSuccess()) {
			return object;
		} else {
			return defaultSuplier.get();
		}
	}

	public Result<T> rethrow() {
		if (throwable != null) {
			ReflectionUtils.sneakyThrow(throwable);
		}
		return this;
	}

	public T getObjectNoThrow() {
		return object;
	}

	public Optional<T> asOptionalNoThrow() {
		return Optional.ofNullable(object);
	}

	public Throwable getThrowable() {
		return throwable;
	}

}
